/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.ead.ifc.aplicandojavarmi;

/**
 *
 * @author gutavo
 */
import java.rmi.Naming;

public class MotoristaClient {
    public static void main(String[] args) {
        try {
            for (int i = 1; i <= 8; i++){
                Estacao estacao = (Estacao)
                    Naming.lookup("estacao" + i);

                estacao.registrar();
                Thread.sleep(2000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}