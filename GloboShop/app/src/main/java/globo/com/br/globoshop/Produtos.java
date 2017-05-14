package globo.com.br.globoshop;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Produtos {

    @SerializedName("musica")
    @Expose
    private Musica musica;


    public Musica getMusica() {
        return musica;
    }

    public void setMusica(Musica musica) {
        this.musica = musica;
    }

}