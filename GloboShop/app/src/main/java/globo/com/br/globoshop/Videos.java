package globo.com.br.globoshop;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Videos {

@SerializedName("produtos")
@Expose
private Produtos produtos;
@SerializedName("video")
@Expose
private String video;

public Produtos getProdutos() {
return produtos;
}

public void setProdutos(Produtos produtos) {
this.produtos = produtos;
}

public String getVideo() {
return video;
}

public void setVideo(String video) {
this.video = video;
}

}