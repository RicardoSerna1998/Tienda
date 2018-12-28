package com.example.ricardosernam.tienda.Ventas;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class Escanner extends AppCompatActivity implements ZXingScannerView.ResultHandler{  ////abrimos el escanner

    private ZXingScannerView mScannerView;
    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;
    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[] {Manifest.permission.CAMERA};

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        checkPermissions();
        mScannerView = new ZXingScannerView(this);   // aignamos el escaner
        setContentView(mScannerView);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); //lo registramos como handeler para obtener resultados
        mScannerView.startCamera();          // iniciamos la camara
        mScannerView.setAutoFocus(true);
        }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // la pasusamos
    }

    @Override
    //manejar resultado y lo enviamos al fragment
    public void handleResult(Result rawResult) {
        String resultado;
        resultado = rawResult.getText().toString().trim();
        //regresar resultado
        Intent intent = new Intent();
        intent.putExtra("BARCODE", resultado);
        setResult(RESULT_OK, intent);
        finish();
        // si quieres seguir escaneando:
        //mScannerView.resumeCameraPreview(this);
    }
    protected void checkPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            final List<String> missingPermissions = new ArrayList<String>();
            // check all required dynamic permissions
            for (final String permission : REQUIRED_SDK_PERMISSIONS) {
                final int result = ContextCompat.checkSelfPermission(this, permission);
                if (result != PackageManager.PERMISSION_GRANTED) {
                    missingPermissions.add(permission);
                }
            }
            if (!missingPermissions.isEmpty()) {
                // request all missing permissions
                //final String[] permissions = missingPermissions.toArray(new String[missingPermissions.size()]);
                Toast.makeText(this, "Entra", Toast.LENGTH_LONG).show();
                final String[] permissions = missingPermissions.toArray(new String[0]);
                ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_ASK_PERMISSIONS);
            } else {
                final int[] grantResults = new int[REQUIRED_SDK_PERMISSIONS.length];
                Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED);
                onRequestPermissionsResult(REQUEST_CODE_ASK_PERMISSIONS, REQUIRED_SDK_PERMISSIONS, grantResults);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                for (int index = permissions.length - 1; index >= 0; --index) {
                    if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                        // exit the app if one permission is not granted
                        Toast.makeText(this, "Required permission '" + permissions[index]
                                + "' not granted, exiting", Toast.LENGTH_LONG).show();
                        finish();
                        return;
                    }
                }
                Toast.makeText(this, "Permisos asigandos", Toast.LENGTH_LONG).show();
                // all permissions were granted
                //initialize();
                break;
        }
    }
}