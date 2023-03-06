package ec.springboot.app.util.render;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

public class PageRender<T> {
	
	private String url;
	private Page <T> page;
	
	private int numElemPaginas;
	private int totalPages;
	private int pagActual;
	private List<PageItem> paginas;
	
	public PageRender(String url, Page<T> page) {
		this.url = url;
		this.page = page;
		this.paginas = new ArrayList<PageItem>();
		
		numElemPaginas = page.getSize();
		totalPages = page.getTotalPages();
		pagActual = page.getNumber() + 1;
		
		/*
		 * 1.-if Paginador que nos muestra a todas las paginas 
		 * entonces partimos de las pgina 1 hasta las paginas que sean 
		 * 2.- Paginador por rangos desd el else
		 */
		int desde, hasta;
		//Muestra todo el paginador
		if(totalPages <= numElemPaginas) {
			desde = 1;
			hasta = totalPages;
		}else {
			if(pagActual <= numElemPaginas/2) {
				desde = 1;
				hasta = numElemPaginas;
			}else if(pagActual >= totalPages - numElemPaginas/2){
				//Muestra desde donde se queda
				desde = totalPages - numElemPaginas +1;
				hasta = numElemPaginas;
			}else {
				desde = pagActual -  numElemPaginas/2;
				hasta = numElemPaginas;
			}
		}
		
		//aqui se agregan los items
		for(int i = 0 ; i < hasta; i++) {
			paginas.add(new PageItem(desde + i, pagActual == desde+i));
		}
	}

	public String getUrl() {
		return url;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public int getPagActual() {
		return pagActual;
	}

	public List<PageItem> getPaginas() {
		return paginas;
	}
	
	public boolean isFirst() {
		return page.isFirst();
	}
	
	public boolean isLast() {
		return page.isLast();
	}
	
	public boolean isHasNext() {
		return page.hasNext();
	}
	
	public boolean isHasPrevious() {
		return page.hasPrevious();
	}
	

}
