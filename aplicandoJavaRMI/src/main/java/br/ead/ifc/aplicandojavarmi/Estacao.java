/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package br.ead.ifc.aplicandojavarmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author gutavo
 */
public interface Estacao extends Remote{
    void registrar() throws RemoteException;
}
