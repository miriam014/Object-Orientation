package smu;


import smu.DTO.Famiglia;
import smu.DTO.Utente;
import smu.DTO.Carta;
import smu.DTO.ContoCorrente;
import smu.DTO.Categoria;
import smu.DTO.Transazione;
import smu.DTO.Portafoglio;
import smu.DTO.SpeseProgrammate;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {

        // prove da cancellare poi
        Utente utente = new Utente("giulia28", "Giulia", "Gargiulo", "3664842648", "giulia@hotmail.it", "1234","1");
        System.out.println(utente.toString());

        Famiglia famiglia = new Famiglia("1", "Gargiulo");
        System.out.println(famiglia.toString());

        ContoCorrente conto = new ContoCorrente("1234", "IT2472", 273.5f, "BancoPosta", "74829");
        System.out.println(conto.toString());

        Carta carta = new Carta("4636810", "PostePay", LocalDate.of(2025,5,6), 100.0f, "Debito", 100.0f, "123");
        System.out.println(carta.toString());

        Categoria categoria = new Categoria("Alimentari", "Cibo");
        System.out.println(categoria.toString());

        

    }
}