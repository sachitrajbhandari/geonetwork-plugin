package au.org.emii.classifier;

import org.apache.lucene.facet.taxonomy.CategoryPath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CategoryTestHelper {

	public static List<CategoryPath> createCategoryPathList(String... categoryPaths) {
		List<CategoryPath> categories = new ArrayList<CategoryPath>();

		for (String categoryPath: categoryPaths) {
			categories.add(new CategoryPath(categoryPath, '/'));
		}

		return categories;
	}

	public static void assertCategoryListEquals(List<CategoryPath> result, String... categories) {
		assertEquals(categories.length, result.size());
	
		for (CategoryPath resultCategoryPath : result) {
			assertTrue(Arrays.asList(categories).contains(resultCategoryPath.toString()));
		}
	}
	

}
