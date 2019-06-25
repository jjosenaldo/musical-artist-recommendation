package jmeter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import main.Recommender;

public class RecommenderTest extends AbstractJavaSamplerClient{
	@Override
	public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
		SampleResult result = new SampleResult();
		result.setSampleLabel("Test Sample");
		result.sampleStart();
		Map<Integer, Double> newInterests = new ConcurrentHashMap<>();
		newInterests.put(1, 0.5);
		newInterests.put(2, 0.3);
		newInterests.put(3, 0.2);
		Recommender rec = new Recommender(100);
		rec.recommend(newInterests);
		result.sampleEnd();
		result.setResponseCode("200");
		result.setResponseMessage("OK");
		result.setSuccessful(true);
		
		return result;
	}
}
