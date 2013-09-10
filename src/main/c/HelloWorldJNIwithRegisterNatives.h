#include <jni.h>

//----keyval-----
typedef struct keyval{
  char *key;
  void *value;
}keyval;

keyval *keyval_new(char *key, void *value);
keyval *keyval_copy(keyval const *in);
void keyval_free(keyval *in);
int keyval_matches(keyval const *in, char const *key);

//----Dictionary-----


typedef struct dictionary{
  keyval **pairs;
  int length;
}dictionary;

dictionary *dictionary_new (void);
dictionary *dictionary_copy(dictionary *in);
void dictionary_free(dictionary *in);
void dictionary_add(dictionary *in, char *key, void *value); 
void *dictionary_find(dictionary const *in, char const *key); 


//-------------------
#ifdef __cplusplus
extern "C" {
#endif

 
  JNIEXPORT void JNICALL load_dictionary(JNIEnv *, jobject);

  JNIEXPORT void JNICALL add_dictionary(JNIEnv *, jobject, jstring key, jint val);
  JNIEXPORT jint JNICALL find_dictionary(JNIEnv *, jobject, jstring key);
  JNIEXPORT void JNICALL free_dictionary(JNIEnv *, jobject);



  JNIEXPORT jint JNICALL add(JNIEnv *, jobject, jint, jint);
  JNIEXPORT jint JNICALL badd(JNIEnv *, jobject, jint, jint);

  JNIEXPORT jstring JNICALL hello(JNIEnv *, jobject);

#ifdef __cplusplus
}
#endif
