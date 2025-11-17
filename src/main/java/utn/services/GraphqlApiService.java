package utn.services;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import utn.models.dto.HechoDTO;
import utn.models.dto.graphql.GraphQLResponse;
import utn.services.internal.WebApiCallerService;

import java.util.List;
import java.util.Map;

@Service
public class GraphqlApiService {

    private final WebApiCallerService client;

    public GraphqlApiService(WebApiCallerService client) {
        this.client = client;
    }

    public List<HechoDTO> obtenerHechos() {

        String query = """
            query {
                hechos {
                    id
                    titulo
                    descripcion
                    fecha
                }
            }
        """;

        ParameterizedTypeReference<GraphQLResponse<Map<String, List<HechoDTO>>>> type =
                new ParameterizedTypeReference<>() {};

        GraphQLResponse<Map<String, List<HechoDTO>>> response =
                client.callGraphQL("/graphql", query, null, type);

        return response.getData().get("hechos");
    }

    public HechoDTO obtenerHechoPorId(String id) {

        String query = """
            query ($id: ID!) {
                hecho(id: $id) {
                    id
                    titulo
                    descripcion
                    fecha
                }
            }
        """;

        Map<String, Object> vars = Map.of("id", id);

        ParameterizedTypeReference<GraphQLResponse<Map<String, HechoDTO>>> type =
                new ParameterizedTypeReference<>() {};

        GraphQLResponse<Map<String, HechoDTO>> response =
                client.callGraphQL("/graphql", query, vars, type);

        return response.getData().get("hecho");
    }
}
