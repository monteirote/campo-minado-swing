package br.com.cod3r.cm.modelo;

import java.util.ArrayList;
import java.util.List; 

public class Campo {
	
	private final int linha; 
	private final int coluna; 
	
	private boolean aberto = false;
	private boolean minado = false;
	private boolean marcado = false;
	
	private List<Campo> vizinhos = new ArrayList<>();
	private List<CampoObservador> observadores = new ArrayList<>();
	// private List<BiConsumer<Campo, CampoEvento>> observadores2 = new ArrayList<>();
	
	public void registrarObservador(CampoObservador e) {
		observadores.add(e);
	}
	
	private void notificarObservadores(CampoEvento e) {
		observadores.stream()
			.forEach(o -> o.eventoOcorreu(this, e));
	}
	
	
	Campo(int linha, int coluna) {
		this.linha = linha;
		this.coluna = coluna;
	}
	
	
boolean adicionarVizinho2(Campo vizinho) {
		
		boolean adicionado = false;
		boolean linhaDiferente = vizinho.linha != this.linha;
		boolean colunaDiferente = vizinho.coluna != this.coluna;
		boolean diagonal = colunaDiferente && linhaDiferente;
		
		int deltaLinha = Math.abs(this.linha - vizinho.linha);
		int deltaColuna = Math.abs(this.coluna - vizinho.coluna);
		int deltaGeral = deltaLinha + deltaColuna;
		
		if (!diagonal && deltaGeral == 1) {
			vizinhos.add(vizinho);
			adicionado = true;
		}else if (diagonal && deltaGeral == 2) {
			vizinhos.add(vizinho);
			adicionado = true;
		}
		
		return adicionado;
	}
	
	
	
	
	
	boolean adicionarVizinho(Campo vizinho) {
		boolean linhaDiferente = linha != vizinho.linha;
		boolean colunaDiferente = coluna != vizinho.coluna;
		boolean diagonal = colunaDiferente && linhaDiferente;
		
		int deltaLinha = Math.abs(linha - vizinho.linha);
		int deltaColuna = Math.abs(coluna - vizinho.coluna);
		int deltaGeral = deltaLinha + deltaColuna;
		
		if (!diagonal && deltaGeral == 1) {
			vizinhos.add(vizinho);
			return true;
		}else if (diagonal && deltaGeral == 2) {
			vizinhos.add(vizinho);
			return true;
		}
		
		return false;
	}
	
	public void alternarMarcacao() {
		if(!aberto) {
			marcado = !marcado;
			
			if(marcado) {
				notificarObservadores(CampoEvento.MARCAR);	
			} else {
				notificarObservadores(CampoEvento.DESMARCAR);	
			}
		}
	}
	
	public boolean abrir() {
		
		if(!aberto && !marcado) {
			aberto = true;
			
			if (minado) {
				notificarObservadores(CampoEvento.EXPLODIR);
				return true;
			}
			
			setAberto(true);				
			
			if (vizinhancaSegura()) {
				vizinhos.forEach(v -> v.abrir());
			}
			
			return true;
		} else {
			return false;
		}
	}
	
	public boolean vizinhancaSegura() {
		return vizinhos.stream().noneMatch(v -> v.minado);
	}
	
	public boolean isMarcado() {
		return marcado;
	}
	
	void minar() {
		minado = true;
	}
	
	public boolean isMinado() {
		return minado;
	}
	
	void setAberto(boolean aberto) {
		this.aberto = aberto;
		
		if(aberto) {
			notificarObservadores(CampoEvento.ABRIR); 
			
		}
	}

	public boolean isAberto() {
		return aberto;
	}
	
	public boolean isFechado() {
		return !isAberto();
	}
	
	public int getLinha() {
		return linha;
	}
	
	public int getColuna() {
		return coluna;
	}
	
	boolean objetivoAlcancado() {
		boolean desvendado = !minado && aberto;
		boolean protegido = minado && marcado;
		return desvendado || protegido;
	}
	
	public int minasNaVizinhanca() {
		return (int) vizinhos.stream().filter(v -> v.minado).count();
	}
	
	void reiniciar() {
		aberto = false;
		marcado = false;
		minado = false;
		notificarObservadores(CampoEvento.REINICIAR);
	}
	
	
	
}
	
		
	 


