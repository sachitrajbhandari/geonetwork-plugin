package au.org.emii.classifier;

import org.apache.log4j.Logger;
import org.apache.lucene.facet.taxonomy.CategoryPath;
import org.fao.geonet.kernel.ThesaurusFinder;
import org.fao.geonet.kernel.search.classifier.Classifier;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to determine the category paths (facets) to be indexed for an AODN term given its URI
 */

public class UriClassifier implements Classifier  {
    private static Logger logger = Logger.getLogger(UriClassifier.class);

    private final ThesaurusFinder thesaurusFinder; // thesaurus finder
    private final String vocabularyScheme; // vocabulary in which the term is defined
    private final String classificationScheme; // classifier used to create CategoryPaths for a term
    private final String indexKey; // classifier indexKey

    public UriClassifier(ThesaurusFinder thesaurusFinder, String vocabularyScheme, String classificationScheme, String indexKey) {
        this.thesaurusFinder = thesaurusFinder;
        this.vocabularyScheme = vocabularyScheme;
        this.classificationScheme = classificationScheme;
        this.indexKey = indexKey;
    }

    @Override
    public List<CategoryPath> classify(String value) {
        AodnThesaurus vocabularyThesaurus = new AodnThesaurus(thesaurusFinder.getThesaurusByConceptScheme(vocabularyScheme));
        AodnThesaurus classificationThesaurus = new AodnThesaurus(thesaurusFinder.getThesaurusByConceptScheme(classificationScheme));
        AodnTermClassifier termClassifier = new AodnTermClassifier(vocabularyThesaurus, classificationThesaurus);

        AodnTerm term = vocabularyThesaurus.getTerm(value);

        if (term == null) {
            logger.warn(String.format("Could not find term with uri='%s' in vocabulary='%s' discoveryParams='%s=%s'",
                value, vocabularyThesaurus.getThesaurusTitle(), indexKey, value));
            return new ArrayList<CategoryPath>();
        }

        return termClassifier.classify(term);
    }
}
