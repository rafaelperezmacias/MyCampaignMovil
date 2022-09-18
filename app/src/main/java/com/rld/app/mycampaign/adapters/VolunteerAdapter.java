package com.rld.app.mycampaign.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rld.app.mycampaign.models.Volunteer;
import com.rld.app.mycampaign.R;

import java.util.ArrayList;

/**
 *    @version 1.0
 *    Esta clase es la encargada de fungir como adaptador para mostrar los elementos del RecyclerView de voluntarios
 *    ubicado en el MainActivity, para ello creamos una clase interna llamada "ViewHolder" que sirve como contenedor
 *    de nuestro item "item_volunteer_adapter" y la clase principal que hereda de RecyclerView.Adapter<T>, donde
 *    especificamos que nuestro elemento generico sea de la clase anidada.
*/
public class VolunteerAdapter extends RecyclerView.Adapter<VolunteerAdapter.ViewHolder> {

    // Contexto que necesitamos para inflar los elementos dentro del RecyclerView
    private Context context;
    // Listado de elementos a mostrar dentro del RecyclerView, en nuestro caso de tipo "Volunteer"
    private ArrayList<Volunteer> volunteers;

    /**
     * Constructor de la clase principal
     @param context Contexto que necesitamos para crear los items dentro del layout del recycler
     @param volunteers ArrayList con los elementos a mostrar dentro del layout
     */
    public VolunteerAdapter(Context context, ArrayList<Volunteer> volunteers)
    {
        this.context = context;
        this.volunteers = volunteers;
    }

    /**
     * Metodo sobreescrito de la clase heredada (RecyclerView.Adapter<>) que nos sirve para crear una nueva instancia
     * de nuestro objeto a repetir dentro del RecyclerView, por medio del "context" crea una nueva instancia del layout
     * "item_volunteer_adapter" y por medio de nuestra clase anidad ViewHolder casteamos el diseño con la funcionalidad
     * que viene incluida dentro del propio objeto Holder del RecyclerView.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Creamos un nuevo view a traves del LayoutInflater, con el contexto que pasamos en el constructor, y especificamos
        // el item a mostrar, "item_volunteer_adapter" que es el que contiene el diseño del elemento, ademas de establecer el
        // parent (el mismo que nos llega por paramatros al sobreescibir el metodo) y establecemos el "attachToRoot" como
        // "false" (para que permita el repintado de los elementos).
        View view = LayoutInflater.from(context).inflate(R.layout.item_volunteer_adapter, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Metodo sobreescrito de la clase heredada (RecyclerView.Adapter<>), sera donde contengamos toda la logica del RecyclerView,
     * cada vez que un objeto se cargue se ejecutara este metodo.
     */
    @Override
    public void onBindViewHolder(@NonNull VolunteerAdapter.ViewHolder holder, int position) {
        // Lo primero que realizamos es obtener la informacion del elemento a mostrar, por medio de la posicion y nuestra
        // lista de elementos.
        Volunteer volunteer = volunteers.get(position);
        // Y por medio del holder accedemos a los elementos del diseño para mostrar la informacion, en esta seccion tambien
        // vendria la logica de los demas componenetes u algun otro metodo que deseemos ejecutar.
        holder.txtTitle.setText(volunteer.getNames());
    }

    /**
     * Metodo sobreescrito de la clase heredada (RecyclerView.Adapter<>), que regresa el tamaño total de los elementos
     * mostrados en el RecyclerView, este tambien es importante debido a que es el que toma como referencia para inflar
     * los elementos, por lo que tomara el tamaño de nuestra como número maximo de elementos.
     */
    @Override
    public int getItemCount() {
        return volunteers.size();
    }

    /**
     Clase interna anidada que sirve para instanciar los objetos del "item_volunteer_adapter", que es layout que nos
     sirve como modelo del item del elemento a repetir dentro del RecyclerView.
     */
    protected static class ViewHolder extends RecyclerView.ViewHolder {

        // Listado de los componentes a utilizar por cada elemento, estos se encuentran en "item_volunter_adapter".
        private TextView txtTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Simple casteo de cada uno de los elementos a utilizar
            txtTitle = itemView.findViewById(R.id.txt_title);
        }
    }
}
