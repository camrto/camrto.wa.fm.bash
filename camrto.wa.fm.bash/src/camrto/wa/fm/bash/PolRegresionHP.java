package camrto.wa.fm.bash;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;

import org.ejml.data.Complex64F;

public class PolRegresionHP {
	private BigDecimal[] x; // datos
	private BigDecimal[] y;
	private int nDatos;
	BigDecimal[][] m; // matriz de los coeficientes
	BigDecimal[] t; // términos independientes
	public BigDecimal[] a; // polinomio a[0]+a[1]·x+a[2]·x2+...
	public int grado; // grado del polinomio
	private int PRESICION=1;
	public PolRegresionHP(BigDecimal[] x, BigDecimal[] y, int grado) {
		this.x = x;
		this.y = y;
		nDatos = x.length;
		this.grado = grado;
		t = new BigDecimal[grado + 1];
		m = new BigDecimal[grado + 1][grado + 1];
		a = new BigDecimal[grado + 1];
		calculaPolinomio();
	}

	private void coeficientes() {
		BigDecimal[] s = new BigDecimal[2 * grado + 1];
		BigDecimal suma;
		for (int k = 0; k <= 2 * grado; k++) {
			suma = new BigDecimal(0);
			for (int i = 0; i < nDatos; i++) {
				suma= suma.add(potencia(x[i], k));
			}
			s[k] = suma;
		}
		for (int k = 0; k <= grado; k++) {
			suma = new BigDecimal(0);
			for (int i = 0; i < nDatos; i++) {
				suma= suma.add(potencia(x[i], k)).multiply(y[i]);
			}
			t[k] = suma;
		}
		for (int i = 0; i <= grado; i++) {
			for (int j = 0; j <= grado; j++) {
				m[i][j] = s[i + j];
			}
		}
	}

	private BigDecimal potencia(BigDecimal base, int exp) {
		BigDecimal producto = new BigDecimal(1);
		for (int i = 0; i < exp; i++) {
			producto = producto.multiply(base);
		}
		return producto;
	}

	// procedimiento de Seidel
	private PolRegresionHP calculaPolinomio() {
		coeficientes();
		// matriz
		BigDecimal aux;
		BigDecimal minusOne=new BigDecimal(-1.0);
		BigDecimal zero=new BigDecimal(0);
		for (int i = 0; i <= grado; i++) {
			aux = m[i][i].multiply(BigDecimal.ONE);
			for (int j = 0; j <= grado; j++) {
				m[i][j] = m[i][j].divide(aux,PRESICION,BigDecimal.ROUND_HALF_UP).multiply(minusOne);
			}
			t[i] = t[i].divide(aux,PRESICION,BigDecimal.ROUND_HALF_UP);
			m[i][i] = zero;
		}
		// aproximación inicial
		BigDecimal[] p = new BigDecimal[grado + 1];
		p[0] = t[0];
		for (int i = 1; i <= grado; i++) {
			p[i] = zero;
		}
		// aproximaciones sucesivas
		BigDecimal error = zero, maximo = zero, tolerancy=new BigDecimal(0.001);
		
		do {
			maximo = zero;
			for (int i = 0; i <= grado; i++) {
				a[i] = t[i];
				for (int j = 0; j < i; j++) {
					a[i] = a[i].add(m[i][j].multiply(a[j]));
				}
				for (int j = i + 1; j <= grado; j++) {
					a[i] = a[i].add(m[i][j].multiply(p[j]));
				}
				error = a[i].add(p[i].multiply(minusOne)).divide(a[i],PRESICION,BigDecimal.ROUND_HALF_UP).abs();
				
				if (error.compareTo(maximo)==1)
					maximo = error.multiply(BigDecimal.ONE);
			}
			for (int i = 0; i <= grado; i++) {
				p[i] = a[i].multiply(BigDecimal.ONE);
			}
		} while (maximo.compareTo(tolerancy) ==1);
		this.p=p;
		return this;
	}
	private BigDecimal[] p;
	public static void main(String[] args) {
		
		int n=10;
		BigDecimal[] x=new BigDecimal[n];//{0,1,2,3,4,5};
		BigDecimal[] y=new BigDecimal[n];//{0,1,2,3,4,5};
		for(int i=0;i<n;i++){
			x[i]=new BigDecimal(i);
			y[i]=new BigDecimal(new Random().nextDouble()*500);
		}
		PolRegresionHP pl=new PolRegresionHP(x,y,3);
		System.out.println(new PolRegresionHP(x,y,3).toString());
		Complex64F[] complex64fs=PolynomialRootFinder.findRoots(pl.getPolynomial());
		for(int i=0;i<complex64fs.length;i++){
			System.out.println(complex64fs[i].toString());
		}
	}
	
	public double[] getPolynomial(){
		double[] ret=new double[p.length];
		for(int i=0;i<p.length;i++){
			ret[i]=p[i].doubleValue();
		}
		return ret;
	}
	
	public String toString(){
		String ret="[ ";
		for(int i=0;i<p.length;i++){
			ret+="("+i+")"+p[i]+(i==p.length-1?"":", ");
		}
		ret+=" ]";
		return ret;
	}
}