package utn.models.dto.graphql;

import lombok.Data;

import java.util.List;

@Data
public class GraphQLResponse<T> {
    private T data;
    private List<GraphQLError> errors;
}