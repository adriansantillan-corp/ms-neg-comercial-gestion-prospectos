package com.nitro.neg.comercial.gestionprospectos;

import org.springframework.boot.SpringApplication;

public class TestMsNegComercialGestionProspectosApplication {

	public static void main(String[] args) {
		SpringApplication.from(MsNegComercialGestionProspectosApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
