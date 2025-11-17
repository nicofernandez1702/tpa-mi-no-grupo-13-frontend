package utn.models.dto.graphql;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class GraphQLError {
    private String message;
    private List<String> path;
    private Map<String, Object> extensions;
}