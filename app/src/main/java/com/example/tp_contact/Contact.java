package com.example.tp_contact;


import java.io.Serializable;

public class Contact implements Serializable, Comparable<Contact> {
    private String nom;
    private String prenom;
    private String numero;

    public Contact(String nom, String prenom, String numero){
        this.nom=nom;
        this.prenom=prenom;
        this.numero=numero;
    }

    public int compareTo(Contact c){
       int comp=this.prenom.compareToIgnoreCase(c.prenom);
       if (comp==0){
           comp=this.nom.compareToIgnoreCase(c.nom);
       }
       return comp;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    @Override
    public String toString() {
        String str=prenom+" " + nom;
        return str;
    }
}
