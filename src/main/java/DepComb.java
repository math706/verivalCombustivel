import java.util.ArrayList;
import java.util.Arrays;

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
        if (qtdade<=0) return -1;
        boolean temCapacidade = (this.qtdaAditivo + qtdade) <= MAX_ADITIVO;
        if(temCapacidade){
            this.qtdaAditivo = qtdaAditivo + qtdade;
            return qtdade;
        }else {
            int empty = MAX_ADITIVO - qtdaAditivo;
            this.qtdaAditivo = MAX_ADITIVO;
            return empty;
        }
    }

    public int recebeGasolina(int qtdade) {
        if (qtdade<=0) return -1;
        boolean temCapacidade = (this.qtdaGasolina + qtdade) <= MAX_GASOLINA;
        if(temCapacidade){
            this.qtdaGasolina = qtdaGasolina + qtdade;
            return qtdade;
        }else {
            int empty = MAX_GASOLINA - qtdaGasolina;
            this.qtdaGasolina = MAX_GASOLINA;
            return empty;
        }
    }

    public int recebeAlcool(int qtdade) {
        if (qtdade<=0) return -1;
        boolean temCapacidade = (qtdaAlcool1+ qtdaAlcool2 + qtdade) <= MAX_ALCOOL;
        if(temCapacidade){
            int alcoolQtdMetade = qtdade/2;
            this.qtdaAlcool1 = qtdaAlcool1 + alcoolQtdMetade;
            this.qtdaAlcool2 = qtdaAlcool2 + alcoolQtdMetade;
            return alcoolQtdMetade;
        }else {
            int empty = MAX_ALCOOL - (qtdaAlcool1+ qtdaAlcool2);
            this.qtdaAlcool1 = MAX_ALCOOL/2;
            this.qtdaAlcool2 = MAX_ALCOOL/2;
            return empty;
        } }

//O método “encomendaCombustivel” é usado quando o centro de distribuição recebe o pedido de um posto. Este método
// recebe por parâmetro a quantidade solicitada pelo posto e o tipo do posto. Se o pedido puder ser atendido, o método
// retorna um arranjo com a quantidade de combustível remanescente em cada tanque, depois do pedido atendido. As
// quantidades devem ser retornadas pela ordem: aditivo, gasolina, álcool T1 e álcool T2. A primeira posição do
// arranjo é usada também para indicar códigos de erro. No caso de ser recebido um valor inválido por parâmetro deve-se
// retornar “-7” na primeira posição do arranjo, se o pedido não puder ser atendido em função da “situação” retorna-se
// “-14” e, caso não haja combustível suficiente para completar a mistura, retorna-se “-21”. Por simplicidade trabalha-se
// apenas com números inteiros. Na hora de fazer os cálculos multiplique os valores por 100. Depois de feitos os cálculos
// dividam por 100 novamente e despreze a parte fracionária;


//    : 5% de aditivo, 25% de álcool e 70% de gasolina pura.
    public int[] encomendaCombustivel(int qtdade, TIPOPOSTO tipoPosto) {
        int [] arranjo = new int[4];
        Arrays.fill(arranjo,0);
// Se o pedido puder ser atendido, o método
// retorna um arranjo com a quantidade de combustível remanescente em cada tanque, depois do pedido atendido. As
// quantidades devem ser retornadas pela ordem: aditivo, gasolina, álcool T1 e álcool T2.
        if (qtdade<=0) {
            arranjo[0]= -7; // No caso de ser recebido um valor inválido por parâmetro deve-se retornar “-7”
            return arranjo;
        }
        int qtdadeAux = qtdade*100;
        int qtdAlcool = 0;
        int qtdGasolina = 0;
        int qtdAditivo = 0;
//   Como os tanques são pequenos a ilha mantém um sistema para controlar os estoques. Quando todos os tanques estiverem
//   acima de 50% da capacidade o sistema opera em modo NORMAL e as encomendas são entregues normalmente para qualquer
//   tipo de posto.
        if (situacao==SITUACAO.NORMAL) {
            qtdAditivo=(int) Math.ceil(qtdadeAux*0.5/100);
            if (qtdAditivo>qtdaAditivo){
                arranjo[0] = -21;  // caso não haja combustível suficiente para completar a mistura, retorna-se “-21”
                return arranjo;
            }
            arranjo[0] = qtdaAditivo -qtdAditivo;

            qtdGasolina=(int) Math.ceil(qtdadeAux*0.7/100);
            if (qtdGasolina>qtdaGasolina) {
                arranjo[0] = -21;  // caso não haja combustível suficiente para completar a mistura, retorna-se “-21”
                return arranjo;
            }
            arranjo[1] = qtdaGasolina-qtdGasolina;

            qtdAlcool=(int) Math.ceil(qtdadeAux*0.25/100);
            if (qtdAlcool>(qtdaAlcool1+qtdaAlcool2)) {
                arranjo[0] = -21;  // caso não haja combustível suficiente para completar a mistura, retorna-se “-21”
                return arranjo;
            }
            qtdAlcool = qtdAlcool/2;
            qtdaAlcool1 = qtdAlcool;
            qtdaAlcool2 = qtdAlcool;
            arranjo[2] = qtdaAlcool1;
            arranjo[3] = qtdaAlcool2;
            qtdaGasolina = qtdaGasolina-qtdGasolina;
            qtdaAditivo = qtdaAditivo-qtdAditivo;
        }
//   Se o volume armazenado em qualquer um dos tanques cair abaixo de 50% o sistema passa a operar em
//   modo SOBRAVISO. Neste modo o sistema só entrega 50% do que é solicitado pelos postos COMUNS e o total solicitado
//   pelos ESTRATEGICOS.
        if (situacao==SITUACAO.SOBRAVISO){
            if (tipoPosto==TIPOPOSTO.COMUM){
                qtdAditivo=(int) Math.ceil(qtdadeAux*0.5);
                qtdAditivo = (int) Math.ceil(qtdAditivo*0.5/100);
                if (qtdAditivo>qtdaAditivo){
                    arranjo[0] = -21;  // caso não haja combustível suficiente para completar a mistura, retorna-se “-21”
                    return arranjo;
                }
                arranjo[0] = qtdaAditivo-qtdAditivo;

                qtdGasolina=(int) Math.ceil(qtdadeAux*0.7);
                qtdGasolina = (int)Math.ceil(qtdGasolina*0.5/100);
                if (qtdGasolina>qtdaGasolina) {
                    arranjo[0] = -21;  // caso não haja combustível suficiente para completar a mistura, retorna-se “-21”
                    return arranjo;
                }
                arranjo[1] = qtdaGasolina-qtdGasolina;

                qtdAlcool=(int) Math.ceil(qtdadeAux*0.25/100);
                qtdAlcool=(int) Math.ceil(qtdAlcool*0.5/100);
                if (qtdAlcool>(qtdaAlcool1+qtdaAlcool2)) {
                    arranjo[0] = -21;  // caso não haja combustível suficiente para completar a mistura, retorna-se “-21”
                    return arranjo;
                }
                qtdAlcool = qtdAlcool/2;
                qtdaAlcool1 = qtdAlcool;
                qtdaAlcool2 = qtdAlcool;
                arranjo[2] = qtdaAlcool1;
                arranjo[3] = qtdaAlcool2;
                qtdaGasolina = qtdaGasolina-qtdGasolina;
                qtdaAditivo = qtdaAditivo-qtdAditivo;
            } else {
                qtdAditivo=(int) Math.ceil(qtdadeAux*0.5/100);
                if (qtdAditivo>qtdaAditivo){
                    arranjo[0] = -21;  // caso não haja combustível suficiente para completar a mistura, retorna-se “-21”
                    return arranjo;
                }
                arranjo[0] = qtdaAditivo -qtdAditivo;

                qtdGasolina=(int) Math.ceil(qtdadeAux*0.7/100);
                if (qtdGasolina>qtdaGasolina) {
                    arranjo[0] = -21;  // caso não haja combustível suficiente para completar a mistura, retorna-se “-21”
                    return arranjo;
                }
                arranjo[1] = qtdaGasolina-qtdGasolina;

                qtdAlcool=(int) Math.ceil(qtdadeAux*0.25/100);
                if (qtdAlcool>(qtdaAlcool1+qtdaAlcool2)) {
                    arranjo[0] = -21;  // caso não haja combustível suficiente para completar a mistura, retorna-se “-21”
                    return arranjo;
                }
                qtdAlcool = qtdAlcool/2;
                qtdaAlcool1 = qtdAlcool;
                qtdaAlcool2 = qtdAlcool;
                arranjo[2] = qtdaAlcool1;
                arranjo[3] = qtdaAlcool2;
                qtdaGasolina = qtdaGasolina-qtdGasolina;
                qtdaAditivo = qtdaAditivo-qtdAditivo;
            }
        }
        if (situacao==SITUACAO.EMERGENCIA){
            if (tipoPosto==TIPOPOSTO.COMUM){
//                se o pedido não puder ser atendido em função da “situação” retorna-se “-14”
                arranjo[0]=-14;
                return arranjo;
            }else {
                qtdAditivo=(int) Math.ceil(qtdadeAux*0.5/100);
                if (qtdAditivo>qtdaAditivo){
                    arranjo[0] = 0;
                }else arranjo[0] = qtdaAditivo - qtdAditivo;

                qtdGasolina=(int) Math.ceil(qtdadeAux*0.7/100);
                if (qtdGasolina>qtdaGasolina) {
                    arranjo[0] = -21;  // caso não haja combustível suficiente para completar a mistura, retorna-se “-21”
                    return arranjo;
                }
                arranjo[1] = qtdaGasolina-qtdGasolina;

                qtdAlcool=(int) Math.ceil(qtdadeAux*0.25/100);
                if (qtdAlcool>(qtdaAlcool1+qtdaAlcool2)) {
                    arranjo[0] = -21;  // caso não haja combustível suficiente para completar a mistura, retorna-se “-21”
                    return arranjo;
                }
                qtdAlcool = qtdAlcool/2;
                qtdaAlcool1 = qtdAlcool;
                qtdaAlcool2 = qtdAlcool;
                arranjo[2] = qtdaAlcool1;
                arranjo[3] = qtdaAlcool2;
                qtdaGasolina = qtdaGasolina-qtdGasolina;
                if (qtdaAditivo-qtdAditivo<0) qtdaAditivo=0;
                else qtdaAditivo = qtdaAditivo-qtdAditivo;
            }
        }
        defineSituacao();
        return arranjo;
    }



//
//   Se o volume armazenado em qualquer um dos tanques cair abaixo de 50% o sistema passa a operar em
//   modo SOBRAVISO. Neste modo o sistema só entrega 50% do que é solicitado pelos postos COMUNS e o total solicitado
//   pelos ESTRATEGICOS. Caso o volume em qualquer um dos tanques caia abaixo de 25%, então as encomendas dos postos
//   COMUNS deixam de ser atendidas e as dos ESTRATEGICOS são atendidas mesmo que não haja aditivo suficiente para
//   compor a mistura. Neste caso o combustível é entregue sem aditivo.
}
