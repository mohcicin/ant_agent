package metier;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import view.PointsCoordinate;

import entite.Ant;
import entite.Arc;
import entite.City;
import entite.Graph;

public class metierimpl implements imetier,Serializable {

	@Override
	public List<Arc> load_seccusseur(City src, Graph g, List<City> m) {
		List<Arc> sec = new ArrayList<Arc>();
		for(Arc a:g.getArcs()){
			if(a.getSrc().getName().equals(src.getName()) &&  !checkcity(a.getDest(), m)){
				sec.add(a);
			}
		}
		return sec;
	}

	@Override
	public Arc linkcity(City s, City d,Graph g) {
		// TODO Auto-generated method stub
		for(Arc a:g.getArcs()){
			if(a.getSrc().getName().equals(s.getName()) &&  a.getDest().getName().equals(d.getName())){
				return a;
			}
		}
		return null;
	}

	@Override
	public boolean checkcity(City f, List<City> m) {
		// TODO Auto-generated method stub
		List<String> cm = new ArrayList<String>();


		for(City c:m){
			cm.add(c.getName());
		}
		
		if(cm.contains(f.getName())) return true;
		return false;
	}

	@Override
	public void updateGlobalPheromone(Graph g)throws Exception {
		// TODO Auto-generated method stub
		
		double old;
		double newph=0;
		File file = new File("phormone.txt");
        /*
		for(Arc ac:g.getArcs()){
			old = ac.getPheromone();
			FileWriter fw = new FileWriter(file, true);
			
	        PrintWriter pw = new PrintWriter(fw);
			pw.println("old phromone for edge "+ac.getSrc().getName() +" \\ "+ac.getDest().getName()+" >> "+ac.getPheromone()+"\n");
			for(Ant an:g.getAnts()){
				for(Arc a:an.getArcs()){
					if(a.getSrc().getName().equals(ac.getSrc().getName()) && a.getDest().getName().equals(ac.getDest().getName())){
						newph = newph + a.getPheromone();
					}
				}
			}
			ac.setPheromone((1-g.getRu())*old + newph);
			pw.println("new phromone for edge "+ac.getSrc().getName() +" \\ "+ac.getDest().getName()+" >> "+ac.getPheromone()+"\n");
			//output.write("****************************************************************** \n");
			pw.close();
		}
		*/
		for (int i = 0; i < g.getArcs().size(); i++) {
			old = g.getArcs().get(i).getPheromone();
			FileWriter fw = new FileWriter(file, true);
			newph =0;
	        PrintWriter pw = new PrintWriter(fw);
			pw.println("old phromone for edge "+g.getArcs().get(i).getSrc().getName() +" \\ "+g.getArcs().get(i).getDest().getName()+" >> "+g.getArcs().get(i).getPheromone()+"\n");
			for(Ant an:g.getAnts()){
				for(Arc a:an.getArcs()){
					if(a.getSrc().getName().equals(g.getArcs().get(i).getSrc().getName()) && a.getDest().getName().equals(g.getArcs().get(i).getDest().getName())){
						newph = newph + a.getPheromone();
					}
				}
			}
			g.getArcs().get(i).setPheromone((1-g.getRu())*old + newph);
			pw.println("new phromone for edge "+g.getArcs().get(i).getSrc().getName() +" \\ "+g.getArcs().get(i).getDest().getName()+" >> "+g.getArcs().get(i).getPheromone()+"\n");
			//output.write("****************************************************************** \n");
			pw.close();
		}
		//output.close();
	}

	@Override
	public double CalcAllPheromone(Graph g) {
		double ph =0;
		for(Ant a:g.getAnts()){
			ph += (1/a.getLongeur());
		}
		return ph;
	}

	@Override
	public double calcprobamove(Arc i, double alpha, double beta) {
		// TODO Auto-generated methowd stub
		double d = Math.pow(i.getPheromone(), alpha)*Math.pow((1/i.getDistance()), beta);
		//System.out.println(">>> "+d + " <<< "+Math.pow(i.getPheromone(), alpha)*Math.pow((1/i.getDistance()), beta));
		return d;
	}

	@Override
	public Arc calculbestmove(City i, Graph g, List<City> m, double alpha,
			double beta) {
		// TODO Auto-generated method stub
		Arc ac = null;
		List<Arc> sec = this.load_seccusseur(i, g, m);
		Map<Arc, Double> map = new HashMap<Arc, Double>();
		double invs,inv;
		List<Double> val = new ArrayList<Double>();
		//System.out.println("From "+i +" IS >> "+sec +" <<< "+sec.size());
		if(sec.size() > 0){
			for(Arc a:sec){
				invs = this.calcprobainvsmove(a.getDest(), sec, alpha, beta);
				if(invs != 0){
					inv = this.calcprobamove(a, alpha, beta)/invs;
					//System.out.println("best move from "+i +" >to >> "+a.getDest().getName()+" is " +inv+" / "+invs);
					map.put(a, inv);
					val.add(inv);
				}
			}
			Collections.sort(val);
			double x;
			for(Arc a:map.keySet()){
				x = map.get(a);
				//System.out.println("Proba arc  "+a +" In all >> "+x);
				if(x == val.get(val.size()-1)){
					ac = a;
				}
			}
		}
		
		return ac;
	}

	@Override
	public double calcprobainvsmove(City i, List<Arc> ar,double alpha,
			double beta) {
		// TODO Auto-generated method stub
		double vl=0;
		for(Arc c:ar){
			if(ar.size() == 1){
				vl = vl + this.calcprobamove(c, alpha, beta);
			}else{
				if(!c.getDest().getName().equals(i.getName())){
					vl = vl + this.calcprobamove(c, alpha, beta);
				}	
			}
			
		}
		return vl;
	}

	/*
	@Override
	public Graph initGraph(double alpha, double beta,double ph0,double ru) {
		// TODO Auto-generated method stub
		Graph gr = new Graph();
		List<City> ct = new ArrayList<City>();
		
		gr.setAlpha(alpha);
		gr.setBeta(beta);
		gr.setPheromone0(ph0);
		gr.setRu(ru);
		
		City a = new City("A");
		City b = new City("B");
		City c = new City("C");
		City d = new City("D");
		City e = new City("E");
		City f = new City("F");
		City g = new City("G");
		
		ct.add(a);
		ct.add(b);
		ct.add(c);
		ct.add(d);
		ct.add(e);
		ct.add(f);
		ct.add(g);
		
		for (City cw:ct) {
			gr.getCities().add(cw);
		}
		
		List<Arc> ac = new ArrayList<Arc>();
		ac.add(new Arc(a, f ,2,ph0));
		ac.add(new Arc(a, b, 4,ph0));
		ac.add(new Arc(a, c,5,ph0));
		ac.add(new Arc(a, d , 3,ph0));
		ac.add(new Arc(a, e , 3,ph0));
		ac.add(new Arc(a, g , 3,ph0));
		
		
		
		ac.add(new Arc(b , a ,4,ph0));
		ac.add(new Arc(b,d,4,ph0));
		ac.add(new Arc(b,f,2,ph0));
		ac.add(new Arc(b,g,3,ph0));
		ac.add(new Arc(b,c,3,ph0));
		ac.add(new Arc(b,e,3,ph0));
		
		ac.add(new Arc(c,a,5,ph0));
		ac.add(new Arc(c,d,6,ph0));
		ac.add(new Arc(c,e,1,ph0));
		ac.add(new Arc(c,b,3,ph0));
		ac.add(new Arc(c,f,1,ph0));
		ac.add(new Arc(c,g,1,ph0));
		
		ac.add(new Arc(d,a,3,ph0));
		ac.add(new Arc(d,b,4,ph0));
		ac.add(new Arc(d,c,6,ph0));
		ac.add(new Arc(d,e,5,ph0));
		ac.add(new Arc(d,g,2,ph0));
		ac.add(new Arc(d,f,2,ph0));
		
		ac.add(new Arc(e,c,1,ph0));
		ac.add(new Arc(e,d,5,ph0));
		ac.add(new Arc(e,g,2,ph0));
		ac.add(new Arc(e,a,3,ph0));
		ac.add(new Arc(e,b,3,ph0));
		ac.add(new Arc(e,f,2,ph0));
		
		ac.add(new Arc(f,a,2,ph0));
		ac.add(new Arc(f,b,2,ph0));
		ac.add(new Arc(f,c,1,ph0));
		ac.add(new Arc(f,d,2,ph0));
		ac.add(new Arc(f,e,2,ph0));
		ac.add(new Arc(f,g,1,ph0));
		
		
		ac.add(new Arc(g,b,3,ph0));
		ac.add(new Arc(g,d,2,ph0));
		ac.add(new Arc(g,e,2,ph0));
		ac.add(new Arc(g,f,1,ph0));
		ac.add(new Arc(g,a,3,ph0));
		ac.add(new Arc(g,c,1,ph0));
		
		for(Arc ar:ac){
			gr.getArcs().add(ar);
		}
		
		return gr;
	}
	*/

	@Override
	public Graph initGraph(double alpha, double beta,double ph0,double ru,List<PointsCoordinate> pc) {
		// TODO Auto-generated method stub
		Graph gr = new Graph();
		List<City> ct = new ArrayList<City>();
		
		gr.setAlpha(alpha);
		gr.setBeta(beta);
		gr.setPheromone0(ph0);
		gr.setRu(ru);
		
		ct = this.initDistance(pc);
		for (City cw:ct) {
			gr.getCities().add(cw);
		}
		
		List<Arc> ac = new ArrayList<Arc>();
		
		
		
		for (int i = 0; i < ct.size(); i++) {
			for (int j = 0; j < ct.size(); j++) {
				if(!ct.get(i).getName().equals(ct.get(j).getName())){
					ac.add(new Arc(ct.get(i), ct.get(j), this.calculdistance(ct.get(i), ct.get(j)), ph0));
				}
			}
		}
		
		for(Arc ar:ac){
			System.out.println(">> "+ar);
			gr.getArcs().add(ar);
		}
		
		return gr;
	}
	@Override
	public double longthOfTour(List<Arc> ac) {
		// TODO Auto-generated method stub
		double vl =0;
		for(Arc a:ac){
			vl += a.getDistance();
		}
		if(vl != 0)return 0.9/vl;
		return 0;
	}

	@Override
	public List<City> initDistance(List<PointsCoordinate> po) {
		// TODO Auto-generated method stub
		List<City> ct = new ArrayList<City>();
		for(PointsCoordinate pc:po){
			ct.add(new City(pc.getPx()+"#"+pc.getPy(), pc));
		}
		return ct;
	}

	@Override
	public double calculdistance(City a, City b) {
		// TODO Auto-generated method stub
		double x = Double.MAX_VALUE;
		double a1 = Math.pow(a.getLatlang().getPx()-b.getLatlang().getPx(), 2);
		double a2= Math.pow(a.getLatlang().getPy()-b.getLatlang().getPy(), 2);
		x = Math.sqrt(a1+a2);
		return x;
	}

	@Override
	public HashMap<Integer, List<Arc>> calculBestTour(
			HashMap<Integer, HashMap<Ant, List<Arc>>> in) {
		// TODO Auto-generated method stub
		/*
		for(Integer i:in.keySet()){
			HashMap<Ant, List<Arc>> d = new HashMap<Ant, List<Arc>>();
			d = in.get(i);
			for(Ant a:d.keySet()){
				System.out.println("ant hopa >> "+a.getName()+" >> "+d.get(a));
			}
			
		}
		*/
		HashMap<Integer, List<Arc>> res = new HashMap<Integer, List<Arc>>();
		HashMap<Integer, List<Arc>> res2 = new HashMap<Integer, List<Arc>>();
		List<Double> b = new ArrayList<Double>();
		HashMap<Double, List<Arc>> tmp = new HashMap<Double, List<Arc>>();
		for(Integer i:in.keySet()){
			HashMap<Ant, List<Arc>> d = new HashMap<Ant, List<Arc>>();
			d = in.get(i);
			HashMap<Ant, Double> tr = new HashMap<Ant, Double>();
			b = new ArrayList<Double>();
			for(Ant a:d.keySet()){
				double x = CalclongthOfTour(d.get(a));
				//tr = new HashMap<Ant, Double>();
				//tr.put(a,x );
				//b.add(x);
				tmp.put(x, d.get(a));
			}
			/*
			Collections.sort(b);
			
			double me;
			for(Ant c:tr.keySet()){
				me = tr.get(c);
				if( me == b.get(0)){
					res.put(i, d.get(c));
				}
			}
			*/
		}
		
		SortedSet<Double> keys = new TreeSet<Double>(tmp.keySet());
		res2.put(0, tmp.get(keys.first()));
		
		
		
		return res2;
	}

	@Override
	public double CalclongthOfTour(List<Arc> ac) {
		// TODO Auto-generated method stub
		double vl =0;
		for(Arc a:ac){
			vl += a.getDistance();
		}
		
		return vl;
	}
}
