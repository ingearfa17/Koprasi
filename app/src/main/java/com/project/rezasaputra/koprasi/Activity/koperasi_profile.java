package com.project.rezasaputra.koprasi.Activity;

/**
 * Created by Muhammad on 5/2/2018.
 */
public class koperasi_profile {
    private String id;
    private String nama;
    private String badanhukum;
    private String alamat;
    private String tlp;

    public koperasi_profile(String id, String nama, String badanhukum, String alamat, String tlp) {
        this.id = id;
        this.nama = nama;
        this.badanhukum = badanhukum;
        this.alamat = alamat;
        this.tlp = tlp;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getBadanhukum() { return badanhukum; }

    public void setBadanhukum(String badanhukum) {
        this.badanhukum = badanhukum;
    }

    public String getAlamat() { return  alamat; }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getTlp() { return tlp; }

    public void setTlp(String tlp) {
        this.tlp = tlp;
    }

    //to display object as a string in spinner
    @Override
    public String toString() {
        return nama;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof koperasi_profile){
            koperasi_profile c = (koperasi_profile )obj;
            if(c.getName().equals(nama) && c.getId()==id ) return true;
        }

        return false;
    }
}

