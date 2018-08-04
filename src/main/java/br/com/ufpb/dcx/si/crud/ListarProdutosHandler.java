package br.com.ufpb.dcx.si.crud;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import br.com.ufpb.dcx.si.ApiGatewayResponse;
import br.com.ufpb.dcx.si.Response;
import br.com.ufpb.dcx.si.dao.ProdutoDAO;
import br.com.ufpb.dcx.si.model.Produto;

public class ListarProdutosHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private ProdutoDAO produtoDAO;
	private final Logger logger = Logger.getLogger(this.getClass());

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		try {
			produtoDAO = new ProdutoDAO();
			// recupera todos os produtos
			List<Produto> produtos = produtoDAO.list();

			// envia resposta de volta
			return ApiGatewayResponse.builder().setStatusCode(200).setObjectBody(produtos)
					.setHeaders(
							Collections.singletonMap("X-Powered-By:", "AWS Lambda & Serverless"))
					.build();
		} catch (Exception ex) {
			logger.error("Erro ao listar produtos: " + ex);

			// envia resposta de ERRO de volta
			Response responseBody = new Response("Erro ao listar produtos: ", input);
			return ApiGatewayResponse.builder().setStatusCode(500).setObjectBody(responseBody)
					.setHeaders(
							Collections.singletonMap("X-Powered-By:", "AWS Lambda & Serverless"))
					.build();
		}
	}

}
