package haw.aip3.haw.services;

import java.util.Date;

import haw.aip3.haw.entities.Angebot;
import haw.aip3.haw.entities.Bauteil;
import haw.aip3.haw.repositories.AngebotRepository;
import haw.aip3.haw.repositories.BauteilRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = haw.aip3.haw.services.AngebotMockTest.ContextConfiguration.class)
public class AngebotMockTest {

	// no @Configuration, so it does only get picked up by this test
	@ComponentScan(basePackages = "haw.aip3.haw.services")
	// Note: there is no @PropertySource("classpath:application.properties") as
	// this is taken from DatabaseConfig.class
	static class ContextConfiguration {
//
//		@Autowired
//		protected BauteilRepository bauteilRepo;
//
//		@Bean(name = "angebotService")
//		public AngebotService angebotService() {
//			return new AngebotServiceImpl() {
//
//				// @Override
//				// public void erstelleAngebot(Bauteil bauteil, Date gueltigBis,
//				// double preis) {
//				// this.angebotRepo.save(new Angebot(bauteil, new Date(),
//				// gueltigBis, preis));
//				// }
//
//				// this method is mocked by our fake implementation
//				@Override
//				public Angebot getAngebot(long bauteilId) {
//					// 2 days = 172800000 ms
//					System.out.println("bauteilId: " + bauteilId);
//					System.out.println("ref: " + bauteilRepo.findOne(bauteilId));
//					return new Angebot(bauteilRepo.findOne(bauteilId), new Date(),
//							new Date(System.currentTimeMillis() + 172800000L),
//							5.4d);
//				}
//			};
//		}
	}

	@Autowired
	private AuftragsService auftragsService;

	@Test
	public void findeAngbeot() throws InterruptedException {
		Angebot a1 = auftragsService.getAngebot(1);
		// angebotService.erstelleAngebot(null);
		Angebot a2 = auftragsService.getAngebot(2);
		System.out.println(a1.getGueltigAb());
		System.out.println(a1.getGueltigBis());
		System.out.println(a1.getPreis());
		System.out.println(a1.getBauteil().getName());
		
		System.out.println(a2.getGueltigAb());
		System.out.println(a2.getGueltigBis());
		System.out.println(a2.getPreis());
		System.out.println(a2.getBauteil().getName());
	}

}
