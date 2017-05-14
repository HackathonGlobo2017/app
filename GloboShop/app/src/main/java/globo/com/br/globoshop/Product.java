package globo.com.br.globoshop;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Carlos Daniel Drury on 5/14/2017.
 */

public class Product {

    @SerializedName("nome")
    @Expose
    private String nome;
    @SerializedName("imgUrl")
    @Expose
    private String imgUrl;
    @SerializedName("valor")
    @Expose
    private Integer valor;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setValor(Integer valor) {
        this.valor = valor;
    }

    public Integer getValor() {
        return valor;
    }
}
