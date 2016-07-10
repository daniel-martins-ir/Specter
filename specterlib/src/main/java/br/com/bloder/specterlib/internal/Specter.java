package br.com.bloder.specterlib.internal;

import java.lang.reflect.Field;

import br.com.bloder.specterlib.annotation.FieldSpect;

/**
 * Created by bloder on 07/07/16.
 */
public class Specter {

  private Object payload;
  private Object pojo;

  public Specter transform(Object payload) {
    this.payload = payload;
    return this;
  }

  public Specter in(Object pojo) {
    this.pojo = pojo;
    return this;
  }

  public Object withFieldContext() {
    specter(payload, pojo);
    return pojo;
  }

  private void specter(Object originClass, Object destinyClass) {

    Field[] payloadFields = originClass.getClass().getDeclaredFields();
    Field[] pojoFields = destinyClass.getClass().getDeclaredFields();

    for (Field originField : payloadFields) {
      if (originField.isAnnotationPresent(FieldSpect.class)) {
        for(Field destinyField : pojoFields) {
          if(originField.getAnnotation(FieldSpect.class).name().equals(destinyField.getAnnotation(FieldSpect.class).name())) {
            destinyField.setAccessible(true);
            originField.setAccessible(true);
            try {
              if (!destinyField.getType().isPrimitive() && !destinyField.getType().equals(String.class)) {
                specter(originField.get(originClass), destinyField.get(destinyClass));
              } else {
                destinyField.set(destinyClass, originField.get(originClass));
              }
            } catch (IllegalAccessException e) {
              e.printStackTrace();
            }
          }
        }
      }
    }
  }
}
