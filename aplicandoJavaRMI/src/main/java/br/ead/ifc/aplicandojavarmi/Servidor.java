/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.ead.ifc.aplicandojavarmi;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

/**
 *
 * @author gutavo
 */
public class Servidor {
  public static void main (String[] args) {

    try {
        LocateRegistry.createRegistry(1099);
        for (int i = 1; i <= 8; i++){
           EstacaoImpl estacao = new EstacaoImpl(i);

            Naming.rebind("estacao" + i ,estacao);
            System.out.println("Servidor " + i + " pronto");
        }
    } catch (Exception e) {
        System.out.println ("Erro no servidor: " + e.getMessage()); 
    }  
 }
}
