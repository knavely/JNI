#include "HelloWorldJNIwithRegisterNatives.h"
#include <stdlib.h>
#include <strings.h>

 dictionary * dict;
void *dictionary_not_found;
// functions keyval -------------
keyval *keyval_new(char *key, void *value){
  keyval * out = malloc(sizeof(keyval));
  *out = (keyval){.key = key, .value = value};
  return out;
} 

keyval *keyval_copy(keyval const *in){
  keyval * out = malloc(sizeof(keyval));
  *out = *in;
  return out;
}

void keyval_free(keyval *in) { free(in);}

int keyval_matches(keyval const *in, char const *key){
  return !strcasecmp(in->key,key);
}
// --------------------Dictionary--------
dictionary *dictionary_new (void){
  static int dnf;
  if(!dictionary_not_found) dictionary_not_found = &dnf;
  dictionary *out = malloc(sizeof(dictionary));
  *out = (dictionary){ }; 
  return out;
}

static void dictionary_add_keyval(dictionary *in, keyval *kv){
  printf("val %i \n",*(int*)kv->value);
  in->length++;
  in->pairs = realloc(in->pairs, sizeof(keyval*) * in->length);
  in->pairs[in->length-1] = kv;
  printf("val %i \n",*(int*)in->pairs[in->length-1]->value);
}

void dictionary_free(dictionary *in){
  for(int i = 0; i < in->length; ++i)
    keyval_free(in->pairs[i]);
  free(in);
}

void dictionary_add(dictionary *in, char *key, void *value){
  if(!key){fprintf(stderr, "NULL is not a valid key.\n"); abort();}
  //printf("%i\n",*(int*)value);
  dictionary_add_keyval(in,keyval_new(key,value));
}

void *dictionary_find(dictionary const *in, char const *key){
  for(int i = 0; i < in->length; ++i)
    if(keyval_matches(in->pairs[i], key))
      {
	//printf("%i",*(int*)in->pairs[i]->value);
	return in->pairs[i]->value; 
      }
  return dictionary_not_found;
} 

dictionary *dictionary_copy(dictionary *in){
  dictionary *out = dictionary_new();
  for(int i = 0; i < in->length; ++i)
    dictionary_add_keyval(out,keyval_copy(in->pairs[i]));
  return out;
}
//---------------------------------JNI Interface

JNIEXPORT jstring JNICALL hello(JNIEnv * env, jobject obj) {
  return (*env)->NewStringUTF(env, "Hello World asdk;lsad;lk;kls!");
}

JNIEXPORT jint JNICALL add(JNIEnv * env, jobject obj, jint value1, jint value2) {
  return (value1 + value2);
}

JNIEXPORT jint JNICALL badd(JNIEnv * env, jobject obj, jint value1, jint value2) {
  return (value1 + value2);
}

JNIEXPORT jint JNICALL find_dictionary(JNIEnv * env, jobject j, jstring key){
  // printf("%i",*(int*)dictionary_find(dict,(*env)->GetStringUTFChars(env,key,0)));
  return *(jint*)dictionary_find(dict,(*env)->GetStringUTFChars(env,key,0)); 
}

JNIEXPORT void JNICALL load_dictionary(JNIEnv *env, jobject j){
  dict = dictionary_new();
}

JNIEXPORT void JNICALL add_dictionary(JNIEnv *env, jobject j, jstring key, jint val){
  int* v = malloc(sizeof(int));
  *v = (int)val;
  //printf("vvv %i \n",*v);
  dictionary_add(dict,(*env)->GetStringUTFChars(env,key,0), v);
}

JNIEXPORT void JNICALL free_dictionary(JNIEnv *env, jobject j){
  dictionary_free(dict);
}

//---------------
/*
 * Table of methods associated with the DrmRawContent class.
 */
static JNINativeMethod HelloWorldMethods[] = {
    /* name, signature, funcPtr */
  {"hello", "()Ljava/lang/String;", (void*)hello},
    {"add", "(II)I", (void*)add},
  {"badd", "(II)I", (void*)badd},
  {"load_dictionary","()V",(void*)load_dictionary},
  {"free_dictionary","()V",(void*)free_dictionary},
  {"add_dictionary","(Ljava/lang/String;I)V",(void*)add_dictionary},
  {"find_dictionary","(Ljava/lang/String;)I",(void*)find_dictionary}
   
   //    {"load_dictionary","()",(void*)load_dictionary},
   // {"add_dictionary","(Ljava/lang/String;I)V",(void*)add_dictionary},
  //{"find","()Ljava/lang/String;",(void*)find}
   // {"free_dictionary", "()V",(void*)free_dictionary},
};

/*
 * Register several native methods for one class.
 */
static int registerNativeMethods(JNIEnv* env, const char* className,
    JNINativeMethod* gMethods, int numMethods)
{
    jclass clazz;

    clazz = (*env)->FindClass(env, className);
    if (clazz == NULL)
        return JNI_FALSE;

    if ((*env)->RegisterNatives(env, clazz, gMethods, numMethods) < 0)
        return JNI_FALSE;

    return JNI_TRUE;
}

/*
 * Register native methods for all classes we know about.
 */
static int registerNatives(JNIEnv* env)
{
  if (!registerNativeMethods(env, "org/digimead/HelloWorldJNIwithRegisterNatives$",HelloWorldMethods,sizeof(HelloWorldMethods) / sizeof(HelloWorldMethods[0])))
    return JNI_FALSE;

  return JNI_TRUE;
}

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved)
{
  JNIEnv* env = NULL;
  jint result = -1;

  if ((*vm)->GetEnv(vm, (void **)&env, JNI_VERSION_1_2) != JNI_OK)
    return JNI_ERR;

  if (!registerNatives(env))
    return JNI_ERR;

  /* success -- return valid version number */
  result = JNI_VERSION_1_4;
  return result;
}
