package globo.com.br.globoshop;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Musica {

    @SerializedName("nome")
    @Expose
    private String nome;
    @SerializedName("tempo")
    @Expose
    private Integer tempo;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("valor")
    @Expose
    private Integer valor;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getTempo() {
        return tempo;
    }

    public void setTempo(Integer tempo) {
        this.tempo = tempo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getValor() {
        return valor;
    }

    public void setValor(Integer valor) {
        this.valor = valor;
    }

}