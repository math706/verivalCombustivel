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
    public SITUACAO situacao;

    public DepComb(int tAditivo, int tGasolina, int tAlcool1, int tAlcool2) {
        this.qtdaAditivo = tAditivo;
        this.qtdaGasolina = tGasolina;
        this.qtdaAlcool1 = tAlcool1;
        this.qtdaAlcool2 = tAlcool2;
        this.defineSituacao();
    }

//  Como os tanques são pequenos a ilha mantém um sistema para controlar os estoques. Quando todos os tanques estiverem
//  acima de 50% da capacidade o sistema opera em modo NORMAL e as encomendas são entregues normalmente para qualquer
//  tipo de posto. Se o volume armazenado em qualquer um dos tanques cair abaixo de 50% o sistema passa a operar em modo
//  SOBRAVISO. Neste modo o sistema só entrega 50% do que é solicitado pelos postos COMUNS e o total solicitado pelos
//  ESTRATEGICOS. Caso o volume em qualquer um dos tanques caia abaixo de 25%, então as encomendas dos postos COMUNS deixam
//  de ser atendidas e as dos ESTRATEGICOS são atendidas mesmo que não haja aditivo suficiente para compor a mistura.
//  Neste caso o combustível é entregue sem aditivo.
    public void defineSituacao(){
        if (qtdaGasolina <= (MAX_GASOLINA* 0.25) || qtdaAditivo <= (MAX_ADITIVO+0.25) || (qtdaAlcool1+qtdaAlcool2) <=(MAX_ALCOOL+0.25)){
            this.situacao = SITUACAO.EMERGENCIA;
        } else if(qtdaGasolina <= (MAX_GASOLINA * 0.5) || qtdaAditivo <= (MAX_ADITIVO * 0.5) || (qtdaAlcool1+qtdaAlcool2) <= (MAX_ALCOOL * 0.5)){
            this.situacao = SITUACAO.SOBRAVISO;
        }else{
            this.situacao = SITUACAO.NORMAL;
        }
    }

    public SITUACAO getSituacao(){
        return situacao;
    }

    public int getGasolina(){
        return this.qtdaGasolina;
    }

    public int getAditivo(){
        return this.qtdaAditivo;
    }

    public int getAlcool1(){
        return this.qtdaAlcool1;
    }

    public int getAlcool2(){
        return this.qtdaAlcool2;
    }

//    Os métodos “recebeAditivo”, “recebeGasolina” e “recebeAlcool” são usados quando o centro de distribuição recebe
//    carga dos componentes. Todos recebem por parâmetro a quantidade do componente (aditivo, gasolina ou álcool)
//    recebida e retornam à quantidade que puderam armazenar devido a limitação do tamanho dos tanques e de quanto ainda
//    tinham armazenado. Devem retornar “-1” caso a quantidade recebida por parâmetro seja inválida.
    public int recebeAditivo(int qtdade) {
        if (qtdade<0) return -1;
        boolean temCapacidade = (this.qtdaAditivo + qtdade) <= MAX_ADITIVO;
        if(temCapacidade){
            this.qtdaAditivo = qtdaAditivo + qtdade;
            return qtdade;
        }else {
            int empty = MAX_ADITIVO - qtdaAditivo;
            this.qtdaAditivo = MAX_ADITIVO;
            return qtdade-empty;
        }
    }

    public int recebeGasolina(int qtdade) {
        if (qtdade<0) return -1;
        boolean temCapacidade = (this.qtdaGasolina + qtdade) <= MAX_GASOLINA;
        if(temCapacidade){
            this.qtdaGasolina = qtdaGasolina + qtdade;
            return qtdade;
        }else {
            int empty = MAX_GASOLINA - qtdaGasolina;
            this.qtdaGasolina = MAX_ADITIVO;
            return qtdade-empty;
        }
    }

    public int recebeAlcool(int qtdade) {
        if (qtdade<0) return -1;
        boolean temCapacidade = (this.qtdaAlcool1 + qtdade) <= MAX_GASOLINA;
        if(temCapacidade){
            this.qtdaGasolina = qtdaGasolina + qtdade;
            return qtdade;
        }else {
            int empty = MAX_GASOLINA - qtdaGasolina;
            this.qtdaGasolina = MAX_ADITIVO;
            return qtdade-empty;
        } }

    public int[] encomendaCombustivel(int qtdade, TIPOPOSTO tipoPosto) { ... }
}
