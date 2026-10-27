/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.ead.ifc.aplicandojavarmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author gutavo
 */
public class EstacaoImpl extends UnicastRemoteObject implements Estacao{
    
    private long id;
    
    public EstacaoImpl(long id)throws RemoteException{
        this.id = id;
    }
    
    @Override
    public void registrar() throws RemoteException {
        System.out.println("Registro na estacao: " + id);
    }
    
}
