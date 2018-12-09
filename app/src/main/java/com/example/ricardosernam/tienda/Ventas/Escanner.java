package com.example.ricardosernam.tienda.Ventas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class Escanner extends AppCompatActivity implements ZXingScannerView.ResultHandler{  ////abrimos el escanner

    private ZXingScannerView mScannerView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);   // aignamos el escaner
        setContentView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); //lo registramos como handeler para obtener resultados
        mScannerView.startCamera();          // iniciamos la camara
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
}