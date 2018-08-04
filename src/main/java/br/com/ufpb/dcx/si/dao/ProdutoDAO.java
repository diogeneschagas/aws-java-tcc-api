package br.com.ufpb.dcx.si.dao;


import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import br.com.ufpb.dcx.si.model.Produto;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

public class ProdutoDAO {


	// faz 'get' do nome da tabela da variável criada em serverless.yml
	private static final String ECOM_PRODUTOS_TABLE_NAME = System.getenv("ECOM_PRODUTOS_TABLE_NAME");

	//	private Produto produto;
	private static DynamoDBAdapter db_adapter;
	private final AmazonDynamoDB client;
	private final DynamoDBMapper mapper;

	private Logger logger = Logger.getLogger(this.getClass());


	public ProdutoDAO() {

		// faz 'build' nas conf. do mapeador 
		DynamoDBMapperConfig mapperConfig = DynamoDBMapperConfig.builder()
				.withTableNameOverride(new DynamoDBMapperConfig.TableNameOverride(ECOM_PRODUTOS_TABLE_NAME))
				.build();

		// faz 'get' no db_adapter
		this.db_adapter = DynamoDBAdapter.getInstance();
		this.client = this.db_adapter.getDbClient();

		// cria as configurações do mapeador do DynamoDB
		this.mapper = this.db_adapter.createDbMapper(mapperConfig);
	}


	// metodos CRUD
	public Boolean ifTableExists() {
		return this.client.describeTable(ECOM_PRODUTOS_TABLE_NAME).getTable().getTableStatus().equals("ACTIVE");
	}

	public List<Produto> list() throws IOException {
		DynamoDBScanExpression scanExp = new DynamoDBScanExpression();
		List<Produto> resultados = this.mapper.scan(Produto.class, scanExp);
		for (Produto p : resultados) {
			logger.info("Produtos - list(): " + p.toString());
		}
		return resultados;
	}

	public Produto get(String id) throws IOException {
		Produto produto = null;

		HashMap<String, AttributeValue> av = new HashMap<String, AttributeValue>();
		av.put(":v1", new AttributeValue().withS(id));

		DynamoDBQueryExpression<Produto> queryExp = new DynamoDBQueryExpression<Produto>()
				.withKeyConditionExpression("id = :v1")
				.withExpressionAttributeValues(av);

		PaginatedQueryList<Produto> result = this.mapper.query(Produto.class, queryExp);
		if (result.size() > 0) {
			produto = result.get(0);
			logger.info("Produtos - get(): produto - " + produto.toString());
		} else {
			logger.info("Produtos - get(): produto - Não encontrado.");
		}
		return produto;
	}

	public void save(Produto produto) throws IOException {
		logger.info("Produtos - save(): " + produto.toString());
		this.mapper.save(produto);
	}

	public Boolean delete(String id) throws IOException {
		Produto produto = null;

		// Verifica se o produto existe, antes de deletar
		produto = get(id);
		if (produto != null) {
			logger.info("Produtos - delete(): " + produto.toString());
			this.mapper.delete(produto);
		} else {
			logger.info("Produtos - delete(): produto - não existe.");
			return false;
		}
		return true;
	}

}
