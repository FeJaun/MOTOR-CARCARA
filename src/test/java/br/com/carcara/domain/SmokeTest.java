package br.com.carcara.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class SmokeTest {
	@Test
	void deveValidarExecucaoDoJunit(){
		assertThat(true).isTrue();
	}

}
