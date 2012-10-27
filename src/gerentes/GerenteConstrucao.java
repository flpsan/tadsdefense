package gerentes;

import entidades.Construcao;
import entidades.Entidade;
import entidades.Pedreiro;

public class GerenteConstrucao {

    private Pedreiro pedreiro;
    private Construcao construindo;
    public Construcao alvoConstrucao;

    public GerenteConstrucao(Pedreiro pedreiro) {
        this.pedreiro = pedreiro;
    }

    public void construindo(Entidade e) {
        if (e instanceof Construcao && e.getTime() == pedreiro.getTime()) {
            paraConstrucao();
            Construcao construcao = (Construcao) e;
            construcao.addPedreiro(pedreiro);
            construindo = construcao;
        }
    }

    public void update() {
        if (alvoConstrucao != null) {
            if (construindo != null) {
                paraConstrucao();
            }
            if (pedreiro.getGerenteMov().getMovs().isEmpty()) {
                if (GMapa.podeAtacar(pedreiro, alvoConstrucao)) {
                    construindo(alvoConstrucao);
                    alvoConstrucao = null;
                    System.out.println("chegou no alvo");
                } else {
                    pedreiro.goTo(GMapa.getCelulaMaisProxima(pedreiro, alvoConstrucao));
                    System.out.println("goTo");
                }
            }
        }
    }

    public void paraConstrucao() {
        if (construindo != null) {
            System.out.println("para construcao");
            construindo.removePedreiro(pedreiro);
            construindo = null;
        }
    }
}
