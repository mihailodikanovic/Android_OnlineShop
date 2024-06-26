//
// Created by Mihailo on 02-Jun-24.
//

#include "mihailo_dikanovic_shoppinglist_JNIexample.h"

JNIEXPORT jint JNICALL Java_mihailo_dikanovic_shoppinglist_JNIexample_sumPrices
(JNIEnv *env, jobject obj, jobject prices)
{
      jclass listClass = env->GetObjectClass(prices);

      jmethodID getMethod = env->GetMethodID(listClass, "get", "(I)Ljava/lang/Object;");
      jmethodID sizeMethod = env->GetMethodID(listClass, "size", "()I");
      jint sum = 0;

      jint size = env->CallIntMethod(prices, sizeMethod);

      for(jint i = 0; i < size; i++)
      {
            jobject priceObject = env->CallObjectMethod(prices, getMethod, i);

            jclass integerClass = env->FindClass("java/lang/Integer");
            jmethodID intValueMethod = env->GetMethodID(integerClass, "intValue", "()I");

            jint price = env->CallIntMethod(priceObject, intValueMethod);

            sum += price;

            env->DeleteLocalRef(priceObject);
            env->DeleteLocalRef(integerClass);
      }

      return sum;

}