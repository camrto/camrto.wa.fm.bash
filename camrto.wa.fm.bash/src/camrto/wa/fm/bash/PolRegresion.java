package camrto.wa.fm.bash;

import java.util.Random;

import org.ejml.data.Complex64F;

public class PolRegresion {
	private double[] x; // datos
	private double[] y;
	private int nDatos;
	double[][] m; // matriz de los coeficientes
	double[] t; // términos independientes
	public double[] a; // polinomio a[0]+a[1]·x+a[2]·x2+...
	public int grado; // grado del polinomio

	public PolRegresion(double[] x, double[] y, int grado) {
		this.x = x;
		this.y = y;
		nDatos = x.length;
		this.grado = grado;
		t = new double[grado + 1];
		m = new double[grado + 1][grado + 1];
		a = new double[grado + 1];
		calculaPolinomio();
	}

	private void coeficientes() {
		double[] s = new double[2 * grado + 1];
		double suma;
		for (int k = 0; k <= 2 * grado; k++) {
			suma = 0.0;
			for (int i = 0; i < nDatos; i++) {
				suma += potencia(x[i], k);
			}
			s[k] = suma;
		}
		for (int k = 0; k <= grado; k++) {
			suma = 0.0;
			for (int i = 0; i < nDatos; i++) {
				suma += potencia(x[i], k) * y[i];
			}
			t[k] = suma;
		}
		for (int i = 0; i <= grado; i++) {
			for (int j = 0; j <= grado; j++) {
				m[i][j] = s[i + j];
			}
		}
	}

	private double potencia(double base, int exp) {
		double producto = 1.0;
		for (int i = 0; i < exp; i++) {
			producto *= base;
		}
		return producto;
	}

	// procedimiento de Seidel
	private PolRegresion calculaPolinomio() {
		coeficientes();
		// matriz
		double aux;
		for (int i = 0; i <= grado; i++) {
			aux = m[i][i];
			for (int j = 0; j <= grado; j++) {
				m[i][j] = -m[i][j] / aux;
			}
			t[i] = t[i] / aux;
			m[i][i] = 0.0;
		}
		// aproximación inicial
		double[] p = new double[grado + 1];
		p[0] = t[0];
		for (int i = 1; i <= grado; i++) {
			p[i] = 0.0;
		}
		// aproximaciones sucesivas
		double error = 0.0, maximo = 0.0;
		do {
			maximo = 0.0;
			for (int i = 0; i <= grado; i++) {
				a[i] = t[i];
				for (int j = 0; j < i; j++) {
					a[i] += m[i][j] * a[j];
				}
				for (int j = i + 1; j <= grado; j++) {
					a[i] += m[i][j] * p[j];
				}
				error = Math.abs((a[i] - p[i]) / a[i]);
				if (error > maximo)
					maximo = error;
			}
			for (int i = 0; i <= grado; i++) {
				p[i] = a[i];
			}
		} while (maximo > 0.001);
		this.p=p;
		return this;
	}
	private double[] p;
	public static void main(String[] args) {
		
		int n=10;
		double[] x=new double[n];//{0,1,2,3,4,5};
		double[] y=new double[n];//{0,1,2,3,4,5};
		for(int i=0;i<n;i++){
			x[i]=i;
			y[i]=new Random().nextDouble()*500;
		}
		PolRegresion pl=new PolRegresion(x,y,100);
		System.out.println(new PolRegresion(x,y,100).toString());
		Complex64F[] complex64fs=PolynomialRootFinder.findRoots(pl.getPolynomial());
		for(int i=0;i<complex64fs.length;i++){
			System.out.println(complex64fs[i].toString());
		}
	}
	
	public double[] getPolynomial(){
		return p;
	}
	
	public String toString(){
		String ret="[ ";
		for(int i=0;i<p.length;i++){
			ret+=p[i]+(i==p.length-1?"":", ");
		}
		ret+=" ]";
		return ret;
	}
}