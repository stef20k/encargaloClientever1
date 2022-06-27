package com.example.cliente2;

import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.widget.SearchView;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cliente2.Database.Database;
import com.example.cliente2.Model.ItemListCategoria;
import com.example.cliente2.Model.ItemListEsta;
import com.example.cliente2.Model.ItemListOrder;
import com.example.cliente2.Model.ItemListProductos;
import com.example.cliente2.adapter.AdaptadorPromos;
import com.example.cliente2.adapter.IPConfig;
import com.example.cliente2.adapter.RecyclerCategoria;
import com.example.cliente2.adapter.RecyclerProductos;
import com.example.cliente2.ui.ChatFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MostrarProductosTienda extends AppCompatActivity implements Response.ErrorListener, Response.Listener<JSONObject> {
    private TextView tvNombre, tvubicacion, tvcalificacion,tvcantpedido,tvsumatotal;
    private ItemListEsta itemDetail;
    public String idtienda;
    private RecyclerView rvListaCategoria;
    private RecyclerCategoria adapter;
    private RecyclerProductos adapterprod;
    private List<ItemListCategoria> itemListCategorias;
    List<ItemListOrder> cart1;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    Button btnVerListadoPedidos;
    String TAG="Productos";
    String nombretienda,calificaciontienda,ubicaciontienda;
    Button btncerrar;
    SearchView searchView;
    String responseSaved;
    List<ItemListProductos> itemListProductos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_productos_tienda);

        initViews();
        initValues();
        cargarCantPrecio();
btncerrar= findViewById(R.id.btn_cerrar_tienda);
searchView=findViewById(R.id.svBuscarProducto);

        CargarProductos();

searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
    @Override
    public boolean onQueryTextSubmit(String query) {
       // Toast.makeText(MostrarProductosTienda.this, query, Toast.LENGTH_SHORT).show();
        return false;
    }



    @Override
    public boolean onQueryTextChange(String newText) {

        //
        if(!newText.isEmpty()){

            List<ItemListProductos> filter = new ArrayList<>();
            Log.d("hola",itemListProductos.get(0).getDescripcion());

            for(int x=0; x<itemListProductos.size();x++){
                  if(itemListProductos.get(x).getDescripcion().toLowerCase().contains(newText.toLowerCase())){

                    ItemListProductos item= new ItemListProductos(itemListProductos.get(x).getIdproducto(),
                            itemListProductos.get(x).getDescripcion(),
                            itemListProductos.get(x).getPrecio(),
                            itemListProductos.get(x).getImgProducto());
                    filter.add(item);
                }
            }



            adapterprod = new RecyclerProductos(filter);
            rvListaCategoria.setAdapter(adapterprod);
            adapterprod.notifyDataSetChanged();
        }else{

            adapter = new RecyclerCategoria(MostrarProductosTienda.this,itemListCategorias);
            rvListaCategoria.setAdapter(adapter);

        }

        return false;
    }
});
btncerrar.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        finish();
    }
});



        btnVerListadoPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MostrarProductosTienda.this,MiPedidoActivity.class);
                if(itemDetail !=null){
                    i.putExtra("nombreTienda", itemDetail.getNom_tienda());
                    i.putExtra("IdTienda",itemDetail.getId_tienda());
                }else{
                    i.putExtra("nombreTienda", nombretienda);
                    i.putExtra("IdTienda",idtienda);
                }

                startActivity(i);
            }
        });
    }

    private void CargarProductos(){


        String URL = IPConfig.ipServidor +"cliente/ListarProductos.php?idtienda="+idtienda;
        URL = URL.replaceAll(" ", "%20");
        StringRequest requesty = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {



                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray json = jsonObject.getJSONArray("consulta");
                    for (int i=0;i<json.length();i++){
                        jsonObject = json.getJSONObject(i);
                        itemListProductos.add(new ItemListProductos(jsonObject.getString("idtiendaproducto"),jsonObject.getString("descripcion"),jsonObject.getString("precioventa"),R.drawable.imgproductosdef));
                    }
                }catch (JSONException e){

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        request = Volley.newRequestQueue(getApplicationContext());
        request.add(requesty);
    }

    private void cargarCantPrecio(){
        cart1 = new Database(this).getProductosPedidos();
        Double subtotal2 = 0.0;
        int i = 0;
        for (ItemListOrder order:cart1){
            subtotal2+=(Math.round(Double.parseDouble(order.getCant())*Double.parseDouble(order.getPrecio())*100.0)/100.0);
            i=i+1;
        }
        tvcantpedido.setText(String.valueOf(i));
        tvsumatotal.setText("$. "+String.valueOf(subtotal2)+" MX");
        refresh(500);
    }
    private void refresh(int milli){
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                cargarCantPrecio();
            }
        };
        handler.postDelayed(runnable,milli);
    }


    private void initViews(){
        tvNombre = findViewById(R.id.txtNombreTienda);
        tvubicacion = findViewById(R.id.txtUbicacion);
        tvcalificacion = findViewById(R.id.tvCalific);
        rvListaCategoria = findViewById(R.id.rvCategorias);
        rvListaCategoria.setHasFixedSize(true);
        btnVerListadoPedidos = findViewById(R.id.btnconfirmar);
        tvcantpedido = findViewById(R.id.txtcantproductos);
        tvsumatotal = findViewById(R.id.txtsumatotal);

    }
    private void initValues(){
        if(getIntent().getExtras()!=null) {
            itemDetail = (ItemListEsta) getIntent().getExtras().getSerializable("item");

            tvNombre.setText(itemDetail.getNom_tienda());
            tvubicacion.setText(itemDetail.getUbicacion());
            tvcalificacion.setText(itemDetail.getCalific());
            idtienda = itemDetail.getId_tienda();
        }else{

           // idtienda=PromocionesFragment.idtiendas;
            idtienda=AdaptadorPromos.idt;
            Log.d(TAG, "onReposnse " + idtienda);
            TiendaDetalles();

        }

        String URL =  IPConfig.ipServidor +"cliente/ListarCategoriaporID.php?idtienda="+idtienda;
        request = Volley.newRequestQueue(this);
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,URL,null,this,this);
        request.add(jsonObjectRequest);
    }
    private List<ItemListCategoria> getItemListCategorias(JSONObject response){
        List<ItemListCategoria> itemListCategoria = new ArrayList<>();
        JSONArray json = response.optJSONArray("consulta");
        try {
            for (int i=0;i<json.length();i++){
                JSONObject jsonObject = null;
                jsonObject = json.getJSONObject(i);
                itemListCategoria.add(new ItemListCategoria(idtienda,jsonObject.getString("categoria") ));
            }
        }catch (JSONException e){

        }
        return itemListCategoria;
    }

    private void  TiendaDetalles() {

        String url="http://192.168.1.125:2020/APIS/tienda/consultaTienda.php?idtienda="+idtienda;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Reponse 2:" + response);

                try {


                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String nombre = jsonObject1.getString("nombre");
                            String descripcionubicacion= jsonObject1.getString("descripcionubicacion");
                            String calificacion = jsonObject1.getString("calificacion");

                            nombretienda=nombre;
                            ubicaciontienda=descripcionubicacion;
                            calificaciontienda=calificacion;
                            tvNombre.setText(nombretienda);
                            tvubicacion.setText(ubicaciontienda);
                            tvcalificacion.setText(calificaciontienda);

                        } catch (Exception e) {
                            Log.d(TAG, "Reponse 1:" + e.getMessage());
                            Toast.makeText(MostrarProductosTienda.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                        }




                    }



                } catch (Exception e) {
                    Log.d(TAG, "Reponse 2:" + e.getMessage());
                    Toast.makeText(MostrarProductosTienda.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG,"OnErrorResponse: "+error.getMessage());
                Toast.makeText(MostrarProductosTienda.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(MostrarProductosTienda.this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(JSONObject response) {

        LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rvListaCategoria.setLayoutManager(manager);
        itemListCategorias = getItemListCategorias(response);
        adapter = new RecyclerCategoria(MostrarProductosTienda.this,itemListCategorias);
        rvListaCategoria.setAdapter(adapter);
    }
}