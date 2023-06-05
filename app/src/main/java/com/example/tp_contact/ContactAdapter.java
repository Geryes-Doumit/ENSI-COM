package com.example.tp_contact;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import android.widget.Filter;


public class ContactAdapter extends BaseAdapter {
    private CustomFilter filter;
    private Context context;
    private ArrayList<Contact> listecontact = new ArrayList<>();
    private ArrayList<Contact>  searchContact;

    public ContactAdapter(Context cont) {
        this.context = cont;
        Contact A= new Contact("Pravednyi", "Oleg", "+331234567");
        this.listecontact.add(A);
        Contact B= new Contact("Gerberon", "Alex", "+339874565");
        this.listecontact.add(B);
        Contact C= new Contact("Sinatra", "Frank", "+330000000");
        this.listecontact.add(C);
        Contact D= new Contact("Volkov", "Sasha", "+330563245");
        this.listecontact.add(D);
        this.searchContact=this.listecontact;
        trie();
    }

    @Override
    public int getCount() {
        return listecontact.size();
    }

    @Override
    public Object getItem(int i) {
        return listecontact.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void trie(){
        Collections.sort(listecontact);
    }

    @Override
    public void notifyDataSetChanged() {
        trie();
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        // convertView null
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.presentation, null);
        }

// Récupération de l’item à la position
        Contact val = listecontact.get(i);
// Récupération du composant (ici un TextView)
        TextView itemView = convertView.findViewById(R.id.textView);
// Mise en place de l’affichage
        itemView.setText(val.toString());
        return convertView;
    }

    public int size() {
        return listecontact.size();
    }

    @Override
    public boolean isEmpty() {
        return listecontact.isEmpty();
    }

    public Contact get(int index) {
        return listecontact.get(index);
    }

    public Contact set(int index, Contact element) {
        return listecontact.set(index, element);
    }

    public boolean add(Contact contact) {
        return listecontact.add(contact);
    }

    public void add(int index, Contact element) {
        listecontact.add(index, element);
    }

    public Contact remove(int index) {
        return listecontact.remove(index);
    }

    public boolean remove(@Nullable Object o) {
        return listecontact.remove(o);
    }

    public void clear() {
        listecontact.clear();
    }

    public Filter getFilter(){
        if (this.filter==null){
            this.filter = new CustomFilter();
        }
        return this.filter;
    }

    class CustomFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint){
            FilterResults results=new FilterResults();

            if (constraint !=null && constraint.length() > 0){
                constraint=constraint.toString().toLowerCase();
                ArrayList<Contact> filters = new ArrayList<>();
                for (int i=0; i< searchContact.size(); i++){
                    if (searchContact.get(i).toString().toLowerCase().contains(constraint)){
                        filters.add(searchContact.get(i));
                    }
                }
                results.count=filters.size();
                results.values=filters;
            } else {
                results.count=searchContact.size();
                results.values=searchContact;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results){
            listecontact = (ArrayList<Contact>) results.values;
            notifyDataSetChanged();
        }
    }
}
