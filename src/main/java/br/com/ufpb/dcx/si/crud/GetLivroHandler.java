package br.com.ufpb.dcx.si.crud;

import java.util.Collections;
import java.util.Map;

import org.apache.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import br.com.ufpb.dcx.si.ApiGatewayResponse;
import br.com.ufpb.dcx.si.Response;
import br.com.ufpb.dcx.si.dao.LivroDAO;
import br.com.ufpb.dcx.si.model.Livro;

public class GetLivroHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private LivroDAO livroDAO;
	private final Logger logger = Logger.getLogger(this.getClass());

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

		try {
			livroDAO = new LivroDAO();
			// obtem 'pathParameters' da entrada informada
			Map<String, String> pathParameters = (Map<String, String>) input.get("pathParameters");
			String livroId = pathParameters.get("id");

			// pega o livro pelo ID
			Livro livro = livroDAO.get(livroId);

			// envia resposta de volta
			if (livro != null) {
				return ApiGatewayResponse.builder().setStatusCode(200).setObjectBody(livro)
						.setHeaders(
								Collections.singletonMap("X-Powered-By:", "AWS Lambda & Serverless"))
						.build();
			} else {
				// envia resposta NOT FOUND
				return ApiGatewayResponse.builder().setStatusCode(404)
						.setObjectBody("Livro com o id: '" + livroId + "' não encontrado.").setHeaders(Collections
								.singletonMap("X-Powered-By:", "AWS Lambda & Serverless"))
						.build();
			}
		} catch (Exception ex) {
			logger.error("Erro ao recuperar o livro: " + ex);

			// envia resposta de ERRO de volta
			Response responseBody = new Response("Erro ao recuperar o livro: ", input);
			return ApiGatewayResponse.builder().setStatusCode(500).setObjectBody(responseBody)
					.setHeaders(
							Collections.singletonMap("X-Powered-By:", "AWS Lambda & Serverless"))
					.build();
		}
	}

}
