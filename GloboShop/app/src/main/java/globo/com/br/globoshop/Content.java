package globo.com.br.globoshop;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Content {

@SerializedName("produtos")
@Expose
private List<Produtos> produtos = null;
@SerializedName("video")
@Expose
private String video;

public List<Produtos> getProdutos() {
return produtos;
}

public void setProdutos(List<Produtos> produtos) {
this.produtos = produtos;
}

public String getVideo() {
return video;
}

public void setVideo(String video) {
this.video = video;
}

}