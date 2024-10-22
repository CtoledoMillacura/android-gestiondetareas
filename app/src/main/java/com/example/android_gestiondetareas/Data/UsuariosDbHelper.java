package com.example.android_gestiondetareas.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UsuariosDbHelper extends SQLiteOpenHelper {

    // Versión de la base de datos
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Usuarios.db";

    // Comando SQL para crear la tabla con id como clave primaria y email único
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + UsuariosContract.UsuarioEntry.TABLE_NAME + " (" +
                    UsuariosContract.UsuarioEntry.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    UsuariosContract.UsuarioEntry.COLUMN_NAME_NOMBRE + " TEXT," +
                    UsuariosContract.UsuarioEntry.COLUMN_NAME_APELLIDOS + " TEXT," +
                    UsuariosContract.UsuarioEntry.COLUMN_NAME_EMAIL + " TEXT UNIQUE," +
                    UsuariosContract.UsuarioEntry.COLUMN_NAME_CONTRASEÑA + " TEXT)";

    // Comando SQL para eliminar la tabla
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + UsuariosContract.UsuarioEntry.TABLE_NAME;

    public UsuariosDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
        insertarUsuarioPorDefecto(db); // Llama al método para insertar el usuario por defecto
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    // Método para registrar un usuario en la base de datos
    public long registrarUsuario(String nombre, String apellidos, String email, String contraseña) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UsuariosContract.UsuarioEntry.COLUMN_NAME_NOMBRE, nombre);
        values.put(UsuariosContract.UsuarioEntry.COLUMN_NAME_APELLIDOS, apellidos);
        values.put(UsuariosContract.UsuarioEntry.COLUMN_NAME_EMAIL, email);
        values.put(UsuariosContract.UsuarioEntry.COLUMN_NAME_CONTRASEÑA, contraseña);

        // Insertar nueva fila, devolver la ID de la fila o -1 si hubo un error
        return db.insert(UsuariosContract.UsuarioEntry.TABLE_NAME, null, values);
    }

    // Método para verificar si las credenciales son correctas
    public boolean loginUsuario(String email, String contraseña) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                UsuariosContract.UsuarioEntry.COLUMN_NAME_ID,
                UsuariosContract.UsuarioEntry.COLUMN_NAME_EMAIL,
                UsuariosContract.UsuarioEntry.COLUMN_NAME_CONTRASEÑA
        };

        String selection = UsuariosContract.UsuarioEntry.COLUMN_NAME_EMAIL + " = ? AND " +
                UsuariosContract.UsuarioEntry.COLUMN_NAME_CONTRASEÑA + " = ?";
        String[] selectionArgs = { email, contraseña };

        Cursor cursor = db.query(
                UsuariosContract.UsuarioEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        boolean loginExitoso = cursor.getCount() > 0;
        cursor.close();

        return loginExitoso;
    }

    // Método para verificar si hay usuarios registrados
    public boolean hayUsuariosRegistrados() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + UsuariosContract.UsuarioEntry.TABLE_NAME, null);

        boolean hayUsuarios = false;
        if (cursor != null) {
            cursor.moveToFirst();
            hayUsuarios = cursor.getInt(0) > 0;
            cursor.close();
        }

        return hayUsuarios;
    }

    // Método para insertar un usuario por defecto
    private void insertarUsuarioPorDefecto(SQLiteDatabase db) {
        // Verificar si el usuario ya existe
        Cursor cursor = db.query(
                UsuariosContract.UsuarioEntry.TABLE_NAME,
                new String[]{UsuariosContract.UsuarioEntry.COLUMN_NAME_ID},
                UsuariosContract.UsuarioEntry.COLUMN_NAME_EMAIL + " = ?",
                new String[]{"admin@example.com"},
                null,
                null,
                null
        );

        if (cursor.getCount() == 0) { // Si no existe, insertar
            ContentValues values = new ContentValues();
            values.put(UsuariosContract.UsuarioEntry.COLUMN_NAME_NOMBRE, "Admin");
            values.put(UsuariosContract.UsuarioEntry.COLUMN_NAME_APELLIDOS, "User");
            values.put(UsuariosContract.UsuarioEntry.COLUMN_NAME_EMAIL, "admin@example.com");
            values.put(UsuariosContract.UsuarioEntry.COLUMN_NAME_CONTRASEÑA, "password123");

            // Insertar nueva fila
            db.insert(UsuariosContract.UsuarioEntry.TABLE_NAME, null, values);
        }
        cursor.close();
    }
}
