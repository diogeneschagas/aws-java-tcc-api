package br.com.ufpb.dcx.si.crud;

import java.util.Collections;
import java.util.Map;

import org.apache.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import br.com.ufpb.dcx.si.ApiGatewayResponse;
import br.com.ufpb.dcx.si.Response;
import br.com.ufpb.dcx.si.dao.ProdutoDAO;
import br.com.ufpb.dcx.si.model.Produto;

public class GetProdutoHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

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
			Produto produto = produtoDAO.get(produtoId);

			// envia resposta de volta
			if (produto != null) {
				return ApiGatewayResponse.builder().setStatusCode(200).setObjectBody(produto)
						.setHeaders(
								Collections.singletonMap("X-Powered-By:", "AWS Lambda & Serverless"))
						.build();
			} else {
				// envia resposta NOT FOUND
				return ApiGatewayResponse.builder().setStatusCode(404)
						.setObjectBody("Produto com o id: '" + produtoId + "' não encontrado.").setHeaders(Collections
								.singletonMap("X-Powered-By:", "AWS Lambda & Serverless"))
						.build();
			}
		} catch (Exception ex) {
			logger.error("Erro ao recuperar o produto: " + ex);

			// envia resposta de ERRO de volta
			Response responseBody = new Response("Erro ao recuperar o produto: ", input);
			return ApiGatewayResponse.builder().setStatusCode(500).setObjectBody(responseBody)
					.setHeaders(
							Collections.singletonMap("X-Powered-By:", "AWS Lambda & Serverless"))
					.build();
		}
	}

}
