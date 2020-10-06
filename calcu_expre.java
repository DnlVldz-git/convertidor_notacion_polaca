
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;


public class calcu_expre {
    
    public static final Integer EOF = -1;
    
    public static void main(String[] args){
        Scanner leer = new Scanner(System.in);
        
        System.out.println("Introduzca el número de expresiones en el archivo");
        int tam = leer.nextInt();
        leer.nextLine();
        System.out.println("Introduzca la dirección del archivo: "); //se pide la dirección del archivo
        String direccion = leer.nextLine();
        lista colita = new lista();
        
        
        
            try{  //con este try se abre el doc que contiene las expresiones
                File file = new File(direccion);        
                try (FileReader reader = new FileReader(file)) {
                    int ascii;
                    for(int i = 0; i<tam; i++){ //se hará un ciclo que se repetirá el número de expresiones que se introduzcan
                        while((ascii =  reader.read()) != EOF){//con ese while se tomará los caracteres del doc y se insertarán a una cola
                            if(ascii == 10) {
                             break;
                            }
                            colita.insertar_1_c((char) ascii);
                        }
                        System.out.println("\n");
                        colita.pasar_polaca_inv_c();//se pasará a polaca la cola actual
                        colita.hacer_null();    //se reiniciará la cola en la que se insertarán 
                        colita.recorrer_c();    //se recorre la cola donde está el resultado
                    }
                }

            }catch (IOException ex) {
                System.out.println("Error al abrir archivo");           
            }
    }
}

class lista{
    nodo_c inicio = null;//se inicializan las referencias utilizadas
    nodo_c inicio_1 = null, inicio_2 = null, fin_1 = null, fin_2 = null;

    
    public void insertar_p(char dato){ //función para insertar a pila
        nodo_c nuevo = new nodo_c();
        nuevo.dato = dato;
        nuevo.prev = null;
        nuevo.sig = null;
        
        if(inicio == null){
            inicio = nuevo;
        }else {
            nuevo.sig = inicio;
            inicio.prev=nuevo;
            inicio = nuevo;
        }
    }
    public boolean checar_vacia_p(){ //función que checa si la pila está vacia
        return(inicio ==null);
    }
    
    public int asignar_valor_p(char dato){//esta función toma un operador y devuevle un número correspondiente a su valor de prioridad o precedencia
        int valor_op =0;
        switch(dato){
            case '^':
                valor_op=3;
            break;
            case '*':
                valor_op=2;
            break;
            case '/':
                valor_op=2;
            break;
            case '+':
                valor_op=1;
            break;
            case '-':
                valor_op=1;
            break;
        }
        return valor_op;
    }
    public void metodo_operador_p(char operador){ //esta función toma un operador como parámetro y dependiendo de su precedencia se hacen diferentes operaciones       
        int valor_op = asignar_valor_p(operador);
        int valor_pila = asignar_valor_p(inicio.dato);
        
        if(operador=='('){ //si es paréntesis de apertura se inserta sin más
            insertar_p(operador);
        }else if(inicio.dato=='('){//si lo que se tiene el princio de la pila es un paréntesis de apertura se insertará el operador sin importar su precedencia
            insertar_p(operador);
        }else if(valor_op==valor_pila){//si es de la misma precedencia se sacará lo que tiene la pila y se insertará a una cola y el operadores se meterá a la pila
            insertar_2_c(inicio.dato);            
            inicio = inicio.sig;
            insertar_p(operador);
        }else if(valor_op<valor_pila){ //si es de menor precedencia, sacará el elemento superior de la pila y lo meterá en una cola, posteriormente volverá a llamar a esta función
            insertar_2_c(inicio.dato);
            inicio = inicio.sig;
            if(inicio!=null&&valor_op<=asignar_valor_p(inicio.dato)){
                metodo_operador_p(operador);
            }else{
                insertar_p(operador);
            }
        } else if(valor_op>valor_pila){
            insertar_p(operador);
        }
    }
    public void sacar_parentesis_p(){//esta función es llamada sacará todo lo que tenga un paréntesis cuando se encuentre uno de cierre
        while(inicio.dato!='('){
            insertar_2_c(inicio.dato);
            inicio=inicio.sig;
        }
        inicio=inicio.sig;
    }
    public void sacar_todo_p(){//está función sacará todos los elementos de la pila
        while(inicio!=null){
            insertar_2_c(inicio.dato);
            inicio=inicio.sig;
        }
    }
    public void insertar_1_c(char dato){//aquí se insertan las expresiones obtenidas
        nodo_c nuevo = new nodo_c();
        nuevo.sig = null;
        nuevo.prev = null;
        nuevo.dato = dato;
        if(inicio_1 == null){
            inicio_1 = nuevo;
            fin_1  = nuevo;
        }else{
            fin_1.sig = nuevo;
            nuevo.prev = fin_1;
            fin_1 = nuevo;
        }
    }
    public void insertar_2_c(char dato){ //aquí se inserta la expresión en notación polaca
        nodo_c nuevo = new nodo_c();
        nuevo.sig = null;
        nuevo.prev = null;
        nuevo.dato = dato;
        if(inicio_2 == null){
            inicio_2 = nuevo;
            fin_2  = nuevo;
        }else{
            fin_2.sig = nuevo;
            nuevo.prev = fin_2;
            fin_2 = nuevo;
            fin_2.sig = null;
        }
    }
    
    public void hacer_null(){ //hace null las referencias para que se vuelvan a insertar valores
        inicio_1 = null;
        fin_1 = null;
    }
    public void recorrer_c(){ //imprime la expresión en polaca
        while(inicio_2!=null){
            System.out.print(inicio_2.dato);
            inicio_2 = inicio_2.sig;
        }
        inicio_2 = null;
        fin_2 = null;
    }
    public void pasar_polaca_inv_c(){//este método sortea si es un número u operador y dependiendo llama a otros métodos
        if(inicio_1!=null){          
            nodo_c aux = inicio_1;
            while(aux!=null){
                if(si_operador_c(aux.dato)){
                    if(checar_vacia_p()){
                        insertar_p(aux.dato);
                    }else if(aux.dato==')'){
                        sacar_parentesis_p();
                    }else {
                        metodo_operador_p(aux.dato);
                    }
                    if(aux.sig ==null){
                        sacar_todo_p();
                    }
                    aux=aux.sig;
                    
                }else{
                    insertar_2_c(aux.dato);
                    if(aux.sig ==null){
                        sacar_todo_p();
                    }
                    aux = aux.sig;
                }
            }            
        }
    }
   
    
    public boolean si_operador_c(char dato){ //esta función es utilizada para checar si es operador o no
        return (dato == '+'||dato == '*'||dato == '-'||dato == '^'||dato == '/'||dato=='('||dato==')');
    }
}

class nodo_c{//la clase nodo para realizar una lista doblemente ligada o una pila o una cola
    char dato;
    nodo_c sig;
    nodo_c prev;
}
