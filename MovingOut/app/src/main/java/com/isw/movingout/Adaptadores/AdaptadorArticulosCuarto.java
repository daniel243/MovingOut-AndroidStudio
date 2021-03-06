package com.isw.movingout.Adaptadores;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.isw.movingout.Daos.daoEtiqueta;
import com.isw.movingout.Objetos.ArticuloCuarto;
import com.isw.movingout.Objetos.Cuarto;
import com.isw.movingout.R;
import com.isw.movingout.Daos.daoArticuloCuarto;

import java.util.ArrayList;


public class AdaptadorArticulosCuarto extends BaseAdapter
{
    ArrayList<ArticuloCuarto> listaArticulosCuarto;
    ArrayList<Cuarto> listaCuartos;
    daoArticuloCuarto clsDaoArticuloCuarto;
    daoEtiqueta clsDaoEtiqueta;
    ArticuloCuarto articulo;
    Activity activity;
    int id = 0;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public AdaptadorArticulosCuarto(Activity a, ArrayList<ArticuloCuarto> listaArticulosCuarto, daoArticuloCuarto daoArticuloCuarto, daoEtiqueta daoEtiqueta, ArrayList<Cuarto> listaCuartos)
    {
        this.listaArticulosCuarto = listaArticulosCuarto;
        this.activity = a;
        this.clsDaoArticuloCuarto = daoArticuloCuarto;
        this.clsDaoEtiqueta = daoEtiqueta;
        this.listaCuartos = listaCuartos;
    }

    @Override
    public int getCount() {
        if (listaArticulosCuarto != null)
            return listaArticulosCuarto.size();
        else
            return 0;
    }

    @Override
    public ArticuloCuarto getItem(int i) {
        articulo = listaArticulosCuarto.get(i);
        return null;
    }

    @Override
    public long getItemId(int i) {
        articulo = listaArticulosCuarto.get(i);
        return articulo.getId();
    }

    @Override
    public View getView(int posicion, View view, ViewGroup viewGroup) {
        View v = view;
        if (v == null)
        {
            LayoutInflater li = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.articulo_cuarto, null);
        }
        articulo = listaArticulosCuarto.get(posicion);
        TextView nombre = (TextView)view.findViewById(R.id.textItemNombre);
        TextView tipo = (TextView) view.findViewById(R.id.textItemTipo);
        Button etiqueta = (Button)view.findViewById(R.id.textItemEtiqueta);
        Button editar = (Button) view.findViewById(R.id.buttonEditItem);

        nombre.setText(articulo.getNombre());
        tipo.setText(articulo.getTipo());
        etiqueta.setText(articulo.getEtiqueta());
        etiqueta.setTag(posicion);
        editar.setTag(posicion);

        etiqueta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = Integer.parseInt(view.getTag().toString());
                final Dialog dialogo = new Dialog(activity);
                dialogo.setTitle("Asignar etiqueta 1 articuloCuarto");
                dialogo.setCancelable(true);
                dialogo.setContentView(R.layout.asignar_etiqueta);
                dialogo.show();

                final Spinner dropdownEtiqueta = (Spinner) dialogo.findViewById(R.id.dropdownAssignEtiqueta);
                String[] spinnerArray = clsDaoEtiqueta.obtenerNombreEtiquetas();
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String> (dialogo.getContext(), android.R.layout.simple_spinner_item, spinnerArray);
                dropdownEtiqueta.setAdapter(spinnerArrayAdapter);

                Button asignar = (Button) dialogo.findViewById(R.id.buttonAssignEtiqueta);
                Button cancelar = (Button) dialogo.findViewById(R.id.buttonCancelAssignEtiqueta);

                articulo = listaArticulosCuarto.get(pos);
                setId(articulo.getId());

                asignar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            articulo = new ArticuloCuarto(getId(), dropdownEtiqueta.getSelectedItem().toString(), null, null);
                            clsDaoArticuloCuarto.asignarEtiqueta(articulo);
                            listaArticulosCuarto = clsDaoArticuloCuarto.verTodos();
                            notifyDataSetChanged();
                            dialogo.dismiss();
                        }catch (Exception e){
                            Toast.makeText(activity, "ERROR", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogo.dismiss();
                    }
                });
            }
        });

        editar.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  int pos = Integer.parseInt(view.getTag().toString());
                  final Dialog dialogo = new Dialog(activity);
                  dialogo.setTitle("Editar registro");
                  dialogo.setCancelable(true);
                  dialogo.setContentView(R.layout.crear_articulocuarto);
                  dialogo.show();
                  final EditText nombre = (EditText) dialogo.findViewById(R.id.inputItemNombre);

                  //Spinner tipo
                  final Spinner dropdownTipo = (Spinner) dialogo.findViewById(R.id.dropdownArticuloCuartoTipo);
                  String[] spinnerTipoArray = {"", "Mueble", "Electrodoméstico", "Decoración", "Planta", "Implemento Gym", "Otros"};
                  ArrayAdapter<String> spinnerTipoArrayAdapter = new ArrayAdapter<String> (dialogo.getContext(), android.R.layout.simple_spinner_item, spinnerTipoArray);
                  dropdownTipo.setAdapter(spinnerTipoArrayAdapter);

                  Button guardar = (Button) dialogo.findViewById(R.id.buttonAddCuarto);
                  Button cancelar = (Button) dialogo.findViewById(R.id.buttonCancelCuarto);
                  Button eliminar = (Button) dialogo.findViewById(R.id.buttonEliminarArticuloCuarto);
                  Button mover = (Button) dialogo.findViewById(R.id.buttonMoverArticuloCuarto);

                  final Spinner dropdownCuartos = (Spinner) dialogo.findViewById(R.id.dropdownMoverArticuloCuarto);
                  String[] spinnerArray = new String[listaCuartos.size() + 1];
                  final Integer[] idCuartosArray = new Integer[listaCuartos.size() + 1];
                  spinnerArray[0] = "";
                  for(int i = 1; i<listaCuartos.size()+1; i++)
                  {
                      spinnerArray[i] = listaCuartos.get(i-1).getNombre();
                      idCuartosArray[i] = listaCuartos.get(i-1).getId();
                  }
                  ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String> (dialogo.getContext(), android.R.layout.simple_spinner_item, spinnerArray);
                  dropdownCuartos.setAdapter(spinnerArrayAdapter);

                  guardar.setText("Editar");
                  articulo = listaArticulosCuarto.get(pos);
                  setId(articulo.getId());
                  nombre.setText(articulo.getNombre());
                  dropdownTipo.setSelection(spinnerTipoArrayAdapter.getPosition(articulo.getTipo()));
                  guardar.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                          try {
                              if (nombre.getText().toString().isEmpty() || dropdownTipo.getSelectedItemPosition() == 0){
                                  if(nombre.getText().toString().isEmpty())
                                      nombre.setError("Este campo es obligatorio");
                                  if (dropdownTipo.getSelectedItemPosition() == 0)
                                      Toast.makeText(activity, "Seleccione un tipo para el articulo", Toast.LENGTH_SHORT).show();
                              }
                              else {
                                  articulo = new ArticuloCuarto(getId(), nombre.getText().toString(), dropdownTipo.getSelectedItem().toString());
                                  clsDaoArticuloCuarto.editar(articulo);
                                  listaArticulosCuarto = clsDaoArticuloCuarto.verTodos();
                                  notifyDataSetChanged();
                                  dialogo.dismiss();
                              }
                          } catch (Exception e) {
                              Toast.makeText(activity, "ERROR", Toast.LENGTH_SHORT).show();
                          }
                      }
                  });
                  cancelar.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                          dialogo.dismiss();
                      }
                  });

                  eliminar.setOnClickListener(new View.OnClickListener()
                  {
                      @Override
                      public void onClick(View view)
                      {
                          AlertDialog.Builder del = new AlertDialog.Builder(activity);
                          del.setMessage("¿Estas seguro de eliminar este articulo?");
                          del.setCancelable(false);
                          del.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                              @Override
                              public void onClick(DialogInterface dialog, int which) {
                                  clsDaoArticuloCuarto.eliminar(getId());
                                  listaArticulosCuarto = clsDaoArticuloCuarto.verTodos();
                                  notifyDataSetChanged();
                                  dialogo.dismiss();
                              }
                          });
                          del.setNegativeButton("No", new DialogInterface.OnClickListener() {
                              @Override
                              public void onClick(DialogInterface dialog, int which) {

                              }
                          });
                          del.show();
                      }
                  });

                  mover.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                          try {
                              if (dropdownCuartos.getSelectedItemPosition() == 0)
                                  Toast.makeText(activity, "Tiene que elegir un cuarto primero", Toast.LENGTH_SHORT).show();
                              else{
                                  articulo = new ArticuloCuarto(getId(), idCuartosArray[dropdownCuartos.getSelectedItemPosition()]);
                                  clsDaoArticuloCuarto.mover(articulo);
                                  listaArticulosCuarto = clsDaoArticuloCuarto.verTodos();
                                  notifyDataSetChanged();
                                  dialogo.dismiss();
                              }
                          } catch (Exception e) {
                              Toast.makeText(activity, "ERROR", Toast.LENGTH_SHORT).show();
                          }
                      }
                  });

              }
          });
        return view;
    }

}
