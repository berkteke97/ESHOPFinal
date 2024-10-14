package com.example.teke.ESHOP.other;

import com.example.teke.ESHOP.model.Product;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ContentBasedRecommender {
    private Directory index;
    private Analyzer analyzer;

    public ContentBasedRecommender() {
        index = new RAMDirectory();
        analyzer = new StandardAnalyzer();
    }

    public void addProduct(Product product) throws IOException {
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        try (IndexWriter writer = new IndexWriter(index, config)) {
            Document doc = new Document();
            doc.add(new TextField("barcode", product.getBarcode(), Field.Store.YES));
            doc.add(new TextField("brand", product.getBrand(), Field.Store.YES));
            doc.add(new TextField("categoryName", product.getCategoryName(), Field.Store.YES));
            doc.add(new TextField("detail", product.getDetail(), Field.Store.YES));
            writer.addDocument(doc);
        }
    }

    public List<Product> recommendSimilarProducts(Product product, int topN) throws IOException, ParseException {
        List<Product> similarProducts = new ArrayList<>();

        QueryParser parser = new QueryParser("description", analyzer);
        Query query = parser.parse(QueryParser.escape(product.getDetail()));

        try (DirectoryReader reader = DirectoryReader.open(index)) {
            IndexSearcher searcher = new IndexSearcher(reader);
            TopDocs results = searcher.search(query, topN);

            for (ScoreDoc scoreDoc : results.scoreDocs) {
                Document doc = searcher.doc(scoreDoc.doc);
                String barcode = doc.get("barcode");
                String brand = doc.get("brand");
                String categoryName = doc.get("categoryName");
                String detail = doc.get("detail");
                similarProducts.add(new Product(barcode, brand, categoryName, detail));
            }
        }

        return similarProducts;
    }
}