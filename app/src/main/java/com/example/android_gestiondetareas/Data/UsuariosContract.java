package com.example.android_gestiondetareas.Data;

import android.provider.BaseColumns;

public final class UsuariosContract {

    private UsuariosContract() {}

    public static class UsuarioEntry implements BaseColumns {
        public static final String TABLE_NAME = "usuario";
        public static final String COLUMN_NAME_ID = "_id";
        public static final String COLUMN_NAME_NOMBRE = "nombre";
        public static final String COLUMN_NAME_APELLIDOS = "apellidos";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_CONTRASEÑA = "contraseña";
    }
}
