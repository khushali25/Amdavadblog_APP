//package Services;
//
//import java.util.ArrayList;
//
//import Core.AppConstants;
//import Core.Helper.ApiHelper;
//import Model.Category;
//import Model.Post;
//import bolts.Task;
//
//public abstract class CacheService {
//
//        public static AppConstants.CacheType CacheTypeName;
//
//        //C# TO JAVA CONVERTER TODO TASK: There is no equivalent in Java to the 'async' keyword:
////ORIGINAL LINE: public static async Task<List<Category>> GetCategories()
//        public static ArrayList<Category> GetCategories() {
//            ArrayList<Category> categories = new ArrayList<Category>();
//            String filePath = AppConstants.CategoryCacheFilePath;
//            try {
//                if (!IsRequiredToReadFromCache(filePath)) {
////C# TO JAVA CONVERTER TODO TASK: There is no equivalent to 'await' in Java:
//                    categories = await ApiHelper.Instance.GetAllCategories();
//                    String json = JsonConvert.SerializeObject(categories);
//                    SaveData(filePath, json);
//                } else {
//                    String json = GetData(filePath);
//                    categories = JsonConvert.<ArrayList<Category>>DeserializeObject(json);
//                }
//            } catch (RuntimeException ex) {
//                FirebaseCrash.Report(ex);
//            }
//            return categories;
//        }
//
//
//        public static ArrayList<Post> GetAllPosts(String lang, boolean isForce) {
//            return GetAllPosts(lang, isForce, 1);
//        }
//
//        public static Task<java.util.ArrayList<Post>> GetAllPosts(String lang) {
//            return GetAllPosts(lang, true, 1);
//        }
//
//        public static Task<java.util.ArrayList<Post>> GetAllPosts() {
//            return GetAllPosts("en", true, 1);
//        }
//
//        //C# TO JAVA CONVERTER TODO TASK: There is no equivalent in Java to the 'async' keyword:
////ORIGINAL LINE: public static async Task<List<Post>> GetAllPosts(string lang="en", bool isForce = true, int page=1)
////C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//        public static ArrayList<Post> GetAllPosts(String lang, boolean isForce, int page) {
//            ArrayList<Post> posts = new ArrayList<Post>();
//            String filePath = AppConstants.PostsCacheFilePath;
//
//            try {
//                if (!IsRequiredToReadFromCache(filePath) || isForce) {
////C# TO JAVA CONVERTER TODO TASK: There is no equivalent to 'await' in Java:
//                    posts = await ApiHelper.Instance.GetAllPosts(page, lang);
//                    String json = JsonConvert.SerializeObject(posts);
//                    if (page == 1) {
//                        SaveData(filePath, json);
//                    } else {
//                        if (json.length() > 5) {
//                            AppendData(filePath, json);
//                        }
//                    }
//                } else {
//                    String json = GetData(filePath);
//                    posts = JsonConvert.<ArrayList<Post>>DeserializeObject(json);
//                }
//            } catch (RuntimeException ex) {
//                FirebaseCrash.Report(ex);
//            }
//            return posts;
//        }
//
//
//        public static Task<java.util.ArrayList<Post>> GetPostsByCategoryId(int categoryId, boolean isForce, String lang) {
//            return GetPostsByCategoryId(categoryId, isForce, lang, 1);
//        }
//
//        public static Task<java.util.ArrayList<Post>> GetPostsByCategoryId(int categoryId, boolean isForce) {
//            return GetPostsByCategoryId(categoryId, isForce, "en", 1);
//        }
//
//        public static Task<java.util.ArrayList<Post>> GetPostsByCategoryId(int categoryId) {
//            return GetPostsByCategoryId(categoryId, true, "en", 1);
//        }
//
//        //C# TO JAVA CONVERTER TODO TASK: There is no equivalent in Java to the 'async' keyword:
////ORIGINAL LINE: internal static async Task<List<Post>> GetPostsByCategoryId(int categoryId, bool isForce = true,string lang = "en", int page=1)
////C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//        public static ArrayList<Post> GetPostsByCategoryId(int categoryId, boolean isForce, String lang, int page) {
//            ArrayList<Post> posts = new ArrayList<Post>();
//            String filePath = AppConstants.PostsByCategoryCacheFilePath(categoryId);
//
//            try {
//                if (!IsRequiredToReadFromCache(filePath) || isForce) {
////C# TO JAVA CONVERTER TODO TASK: There is no equivalent to 'await' in Java:
//                    posts = await ApiHelper.Instance.GetPostsByCategoryId(categoryId, lang, page);
//                    String json = JsonConvert.SerializeObject(posts);
//                    SaveData(filePath, json);
//                } else {
//                    String json = GetData(filePath);
//                    posts = JsonConvert.<ArrayList<Post>>DeserializeObject(json);
//                }
//            } catch (RuntimeException ex) {
//            }
//            return posts;
//        }
//
//
//        public static ArrayList<Post> GetPostsBySearchTerm(String searchTerm) {
//            return GetPostsBySearchTerm(searchTerm, "en");
//        }
//
//        //C# TO JAVA CONVERTER TODO TASK: There is no equivalent in Java to the 'async' keyword:
////ORIGINAL LINE: internal static async Task<List<Post>> GetPostsBySearchTerm(string searchTerm, string lang = "en")
////C# TO JAVA CONVERTER NOTE: Java does not support optional parameters. Overloaded method(s) are created above:
//        public static ArrayList<Post> GetPostsBySearchTerm(String searchTerm, String lang) {
//            ArrayList<Post> posts = new ArrayList<Post>();
//            try {
////C# TO JAVA CONVERTER TODO TASK: There is no equivalent to 'await' in Java:
//                posts = await ApiHelper.getInstance().GetPostByQuery(searchTerm, lang);
//            } catch (RuntimeException ex) {
//                FirebaseCrash.Report(ex);
//            }
//            return posts;
//        }
//    }