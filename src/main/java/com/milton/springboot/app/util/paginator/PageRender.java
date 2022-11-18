package com.milton.springboot.app.util.paginator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

public class PageRender<T> {

	private Page<T> objectPage;
	private String url;
	private int numeroTotalDePaginas;
	private int numeropaginaActual;
	private List<PageItem> paginas;

	public PageRender(String url, Page<T> page) {
		this.objectPage = page;
		this.url = url;

		// Indica el numero total de paginas, el conteo se inicia desde el uno
		this.numeroTotalDePaginas = objectPage.getTotalPages();

		// Indica el numero de la pagina actual, la primera pagina es 0
		this.numeropaginaActual = objectPage.getNumber();

		this.paginas = renderingCalculation(numeroTotalDePaginas, numeropaginaActual);
	}

	public String getUrl() {
		return url;
	}

	public int getNumeroTotalDePaginas() {
		return numeroTotalDePaginas;
	}

	public int getNumeropaginaActual() {
		return numeropaginaActual;
	}

	public List<PageItem> getPaginas() {
		return paginas;
	}

	public boolean isFirst() {
		return objectPage.isFirst();
	}

	public boolean isLast() {
		return objectPage.isLast();
	}

	public boolean hasNext() {
		return objectPage.hasNext();
	}

	public boolean hasPrevious() {
		return objectPage.hasPrevious();
	}

	public List<PageItem> renderingCalculation(int totalPaginas, int paginaActual) {

		int desde;
		int hasta;

		// tamaño del paginador
		int sizePager = 5;
		List<PageItem> items = new ArrayList<PageItem>();

		if (totalPaginas <= sizePager) {
			desde = 1;
			hasta = totalPaginas;
		} else {
			int aux = paginaActual / sizePager;
			desde = (aux * sizePager) + 1;
			hasta = sizePager;

			if ((desde + sizePager) >= (totalPaginas + 1)) {
				hasta = totalPaginas - (desde - 1);
			}

		}

		for (int i = 0; i < hasta; i++) {
			items.add(new PageItem(desde + i, paginaActual + 1 == desde + i));
		}

		return items;
	}

}
