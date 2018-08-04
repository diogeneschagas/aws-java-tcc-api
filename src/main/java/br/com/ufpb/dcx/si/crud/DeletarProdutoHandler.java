package br.com.ufpb.dcx.si.crud;

import java.util.Collections;
import java.util.Map;

import org.apache.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import br.com.ufpb.dcx.si.ApiGatewayResponse;
import br.com.ufpb.dcx.si.Response;
import br.com.ufpb.dcx.si.dao.ProdutoDAO;

public class DeletarProdutoHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private ProdutoDAO produtoDAO;
	private final Logger logger = Logger.getLogger(this.getClass());

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

		try {
			produtoDAO = new ProdutoDAO();
			// obtem 'pathParameters' da entrada informada
			Map<String, String> pathParameters = (Map<String, String>) input.get("pathParameters");
			String produtoId = pathParameters.get("id");

			// pega o produto pelo ID
			Boolean success = produtoDAO.delete(produtoId);

			// envia resposta de volta
			if (success) {
				return ApiGatewayResponse.builder().setStatusCode(204).setHeaders(
						Collections.singletonMap("Aplicacao desenvolvida baseada em", "AWS Lambda & Serverless"))
						.build();

			} else {
				return ApiGatewayResponse.builder().setStatusCode(404)
						.setObjectBody("Produto com id: '" + produtoId + "' não encontrado.").setHeaders(Collections
								.singletonMap("Aplicacao desenvolvida baseada em", "AWS Lambda & Serverless"))
						.build();

			}
		} catch (Exception ex) {
			logger.error("Error in deleting product: " + ex);

			// envia resposta de ERRO de volta
			Response responseBody = new Response("Erro ao deletar produto: ", input);
			return ApiGatewayResponse.builder().setStatusCode(500).setObjectBody(responseBody)
					.setHeaders(
							Collections.singletonMap("Aplicacao desenvolvida baseada em", "AWS Lambda & Serverless"))
					.build();

		}
	}

}
