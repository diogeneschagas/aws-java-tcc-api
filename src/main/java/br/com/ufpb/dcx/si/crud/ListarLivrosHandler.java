package br.com.ufpb.dcx.si.crud;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


import br.com.ufpb.dcx.si.ApiGatewayResponse;
import br.com.ufpb.dcx.si.Response;
import br.com.ufpb.dcx.si.dao.LivroDAO;
import br.com.ufpb.dcx.si.model.Livro;

public class ListarLivrosHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private LivroDAO livroDAO;
	private final Logger logger = Logger.getLogger(this.getClass());

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		try {
			livroDAO = new LivroDAO();
			// recupera todos os livros
			List<Livro> livros = livroDAO.list();

			// envia resposta de volta
			return ApiGatewayResponse.builder().setStatusCode(200).setObjectBody(livros)
					.setHeaders(
							Collections.singletonMap("X-Powered-By:", "AWS Lambda & Serverless"))
					.build();
		} catch (Exception ex) {
			logger.error("Erro ao listar livros: " + ex);

			// envia resposta de ERRO de volta
			Response responseBody = new Response("Erro ao listar livros: ", input);
			return ApiGatewayResponse.builder().setStatusCode(500).setObjectBody(responseBody)
					.setHeaders(
							Collections.singletonMap("X-Powered-By:", "AWS Lambda & Serverless"))
					.build();
		}
	}

}
