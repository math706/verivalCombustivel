public class DepComb {
    public enum SITUACAO { NORMAL, SOBRAVISO, EMERGENCIA }
    public enum TIPOPOSTO { COMUM, ESTRATEGICO }

    private int qtdaAditivo;
    private int qtdaGasolina;
    private int qtdaAlcool1;
    private int qtdaAlcool2;

    public static final int MAX_ADITIVO = 500;
    public static final int MAX_ALCOOL = 2500;
    public static final int MAX_GASOLINA = 10000;
    public static final double PERCENTUAL_ADITIVO = 0.05;
    public static final double PERCENTUAL_ALCOOL = 0.25;
    public static final double PERCENTUAL_GASOLINA = 0.7;


    public DepComb(int tAditivo, int tGasolina, int tAlcool1, int tAlcool2) {
        this.qtdaAditivo = tAditivo;
        this.qtdaGasolina = tGasolina;
        this.qtdaAlcool1 = tAlcool1;
        this.qtdaAlcool2 = tAlcool2;
    }

    public void defineSituacao(){
        if((qtdaAlcool1 + qtdaAlcool2) <= MAX_ALCOOL){
            if(qtdaAlcool1 > qtdaAlcool2){

            }
        }
        if(qtdaAditivo > MAX_ADITIVO * 0.5
                && qtdaGasolina > MAX_GASOLINA * 0.5
                && qtdaAlcool2 > MAX_ALCOOL )
    }

    public SITUACAO getSituacao(){ ;
    }

    public int gettGasolina(){
        return this.qtdaGasolina;
    }

    public int gettAditivo(){
        return this.qtdaAditivo;
    }

    public int gettAlcool1(){
        return this.qtdaAlcool1;
    }

    public int gettAlcool2(){
        return this.qtdaAlcool2;
    }

    public int recebeAditivo(int qtdade) {
        boolean temCapacidade = (this.qtdaAditivo + qtdade) <= MAX_ADITIVO;
        if(temCapacidade){
            this.qtdaAditivo = qtdaAditivo + qtdade;
            return 1;
        }else {
            this.qtdaAditivo = MAX_ADITIVO;
            return 1;
        }
        return null;
    }

    public int recebeGasolina(int qtdade) { ... }

    public int recebeAlcool(int qtdade) { ... }

    public int[] encomendaCombustivel(int qtdade, TIPOPOSTO tipoPosto) { ... }
}
