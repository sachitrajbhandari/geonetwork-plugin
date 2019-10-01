package au.org.emii.classifier;

import org.apache.lucene.facet.taxonomy.CategoryPath;
import org.fao.geonet.kernel.ThesaurusFinder;
import org.fao.geonet.kernel.search.classifier.Classifier;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URL;
import java.util.List;

import static au.org.emii.classifier.CategoryTestHelper.assertCategoryListEquals;
import static org.junit.Assert.assertEquals;

public class LabelClassifierTest {

    private static final String VOCABULARY_SCHEME = "http://www.my.com/test_vocabulary";
    private static final String CLASSIFICATION_SCHEME = "http://www.my.com/test_classification";
    private final String indexKey = "";
    private static ThesaurusFinder thesaurusFinder;

    private Classifier labelClassifier;

    @BeforeClass
    static public void loadThesauri() {
        URL thesauriDirectory = LabelClassifier.class.getResource("/thesauri");
        thesaurusFinder = new ThesaurusDirectoryLoader(thesauriDirectory.getFile());
    }
    
    @Before
    public void setup() {
        labelClassifier = new LabelClassifier(thesaurusFinder, VOCABULARY_SCHEME, CLASSIFICATION_SCHEME, indexKey);
    }

    @Test
    public void testPreferredLabelLookup() {
        List<CategoryPath> result = labelClassifier.classify("Animal Tracking Facility, Integrated Marine Observing System (IMOS)");
        assertCategoryListEquals(result, "Integrated Marine Observing System (IMOS)/Animal Tracking Facility");
    }

    @Test
    public void testAlternateLabelLookup() {
        List<CategoryPath> result = labelClassifier.classify("AATAMS");
        assertCategoryListEquals(result, "Integrated Marine Observing System (IMOS)/Animal Tracking Facility");
    }

    @Test
    public void testMultipleAlternateLabelLookup() {
        List<CategoryPath> result = labelClassifier.classify("SOOP");
        assertCategoryListEquals(result, "Integrated Marine Observing System (IMOS)/Ships of Opportunity Facility (SOOP)");
    }

    @Test
    public void testLabelNotFoundLookup() {
        List<CategoryPath> result = labelClassifier.classify("MISSING");
        assertEquals(0, result.size());
    }

    @Test
    public void testMultipleReplacedBysFound() {
        List<CategoryPath> result = labelClassifier.classify("Australian Institute of Marine Science (AIMS)");
        assertCategoryListEquals(result, "Australian Institute of Marine Science (AIMS) 3");
    }

    @Test
    public void testOneReplacedByFound() {
        List<CategoryPath> result = labelClassifier.classify("Australian Institute of Marine Science (AIMS), Department of Industry, Innovation and Science, Australian Government");
        assertCategoryListEquals(result, "Australian Institute of Marine Science (AIMS) 2");
    }

    @Test
    public void testUsesAltLabelIfPresent() {
        List<CategoryPath> result = labelClassifier.classify("Argo Floats Facility");
        assertCategoryListEquals(result, "Integrated Marine Observing System (IMOS)/Argo Floats Facility");
    }

}
