package br.com.ufpb.dcx.si.crud;

import java.util.Collections;
import java.util.Map;

import org.apache.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.ufpb.dcx.si.ApiGatewayResponse;
import br.com.ufpb.dcx.si.Response;
import br.com.ufpb.dcx.si.dao.ProdutoDAO;
import br.com.ufpb.dcx.si.model.Produto;

public class CadastrarProdutoHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private ProdutoDAO produtoDAO;
	private final Logger logger = Logger.getLogger(this.getClass());

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

		try {
			// faz 'get' na entrada informada no 'body'
			JsonNode body = new ObjectMapper().readTree((String) input.get("body"));

			// cria produto através do POST
			Produto produto = new Produto();
			produtoDAO = new ProdutoDAO();

			produto.setName(body.get("nome").asText());
			produto.setPrice((float)body.get("preco").asDouble());
			
			produtoDAO.save(produto);

			// envia resposta de volta
			return ApiGatewayResponse.builder().setStatusCode(200).setObjectBody(produto)
					.setHeaders(
							Collections.singletonMap("X-Powered-By:", "AWS Lambda & Serverless"))
					.build();

		} catch (Exception ex) {
			logger.error("Erro ao salvar produto: " + ex);

			// envia resposta de ERRO de volta
			Response responseBody = new Response("Erro ao salvar produto: ", input);
			return ApiGatewayResponse.builder().setStatusCode(500).setObjectBody(responseBody)
					.setHeaders(
							Collections.singletonMap("X-Powered-By:", "AWS Lambda & Serverless"))
					.build();
		}
	}
}
