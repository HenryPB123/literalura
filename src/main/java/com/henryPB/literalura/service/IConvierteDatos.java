package com.henryPB.literalura.service;

public interface IConvierteDatos {
    <T> T convertirDatos(String json, Class<T> tClass);
}
