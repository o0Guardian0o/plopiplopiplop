package core;

import java.util.ArrayList;

import ants.QueenAnt;

/**
 * An entire colony of ants and their tunnels.
 *
 * @author Joel
 * @version Fall 2014
 */
public class AntColony {

	public static final String QUEEN_NAME = "AntQueen"; // name of the Queen's place
	public static final int MAX_TUNNEL_LENGTH = 8;

	private int food; // amount of food available
	private QueenPlace queenPlace; // where the queen is
	private ArrayList<Place> places; // the places in the colony
	private ArrayList<Place> beeEntrances; // places which bees can enter (the starts of the tunnels)
	private boolean lv_system; //execute or not the level upping system

	/**
	 * Creates a new ant colony with the given layout.
	 *
	 * @param numTunnels
	 *            The number of tunnels (paths)
	 * @param tunnelLength
	 *            The length of each tunnel
	 * @param moatFrequency
	 *            The frequency of which moats (water areas) appear. 0 means that there are no moats
	 * @param startingFood
	 *            The starting food for this colony.
	 */
	public AntColony (int numTunnels, int tunnelLength, int moatFrequency, int startingFood) {
		//default lv system upping
		this.lv_system = false;
		if (!(this.lv_system)) {
			System.out.println("General Report : \n Lv upping system is not execute for this party");
		}
		
		// simulation values
		food = startingFood;

		// init variables
		places = new ArrayList<Place>();
		beeEntrances = new ArrayList<Place>();
		queenPlace = new QueenPlace(QUEEN_NAME); // magic variable namexw
		
		int counter = 0;
		
		tunnelLength = Math.min(tunnelLength, MAX_TUNNEL_LENGTH); // don't go off the screen!
		// set up tunnels, as a kind of linked-list
		Place curr, prev; // reference to current exit of the tunnel
		for (int tunnel = 0; tunnel < numTunnels; tunnel++) {
			curr = queenPlace; // start the tunnel's at the queen
			for (int step = 0; step < tunnelLength; step++) {
				prev = curr; // keep track of the previous guy (who we will exit to)
				if (counter%moatFrequency == 0 && moatFrequency != 0 && counter!=0) {
					curr = new Water("tunnel[" + tunnel + "-" + step + "]", prev);
					counter ++;
				}
				else {
					curr = new Place("tunnel[" + tunnel + "-" + step + "]", prev); // create new place with an exit that is the previous spot
					counter ++;	
				} 
				prev.setEntrance(curr); // the previous person's entrance is the new spot
				places.add(curr); // add new place to the list
			}
			beeEntrances.add(curr); // current place is last item in the tunnel, so mark that it is a bee entrance
		} // loop to next tunnel

	}
	/**
	 * Ant Colony constructor which allowed you to choose if you want the lv_system
	 * @param numTunnels
	 * 				The number of the tunnels
	 * @param tunnelLength
	 * 				The length of the tunnels
	 * @param moatFrequency
	 * 				The frenquency of the creation of the water place in the colony
	 * @param startingFood
	 * 				The food with which we start the party
	 * @param lvsystem 
	 * 				The boolean which inform if the lvsystem will be used !
	 */				
	public AntColony (int numTunnels, int tunnelLength, int moatFrequency, int startingFood, boolean lvsystem) {
		//lv system initalisation
		this.lv_system = lvsystem;
		if (this.lv_system) {
			System.out.println("General Report : \n Level upping system is lunched for this party !");
		}
		else {
			System.out.println("General Report : \n Level upping system is not lunched for this party !");
		}
		
		// simulation values
		food = startingFood;

		// init variables
		places = new ArrayList<Place>();
		beeEntrances = new ArrayList<Place>();
		queenPlace = new QueenPlace(QUEEN_NAME); // magic variable namexw
		
		int counter = 0;
		
		tunnelLength = Math.min(tunnelLength, MAX_TUNNEL_LENGTH); // don't go off the screen!
		// set up tunnels, as a kind of linked-list
		Place curr, prev; // reference to current exit of the tunnel
		for (int tunnel = 0; tunnel < numTunnels; tunnel++) {
			curr = queenPlace; // start the tunnel's at the queen
			for (int step = 0; step < tunnelLength; step++) {
				prev = curr; // keep track of the previous guy (who we will exit to)
				if (counter%moatFrequency == 0 && moatFrequency != 0 && counter!=0) {
					curr = new Water("tunnel[" + tunnel + "-" + step + "]", prev);
					counter ++;
				}
				else {
					curr = new Place("tunnel[" + tunnel + "-" + step + "]", prev); // create new place with an exit that is the previous spot
					counter ++;	
				} 
				prev.setEntrance(curr); // the previous person's entrance is the new spot
				places.add(curr); // add new place to the list
			}
			beeEntrances.add(curr); // current place is last item in the tunnel, so mark that it is a bee entrance
		} // loop to next tunnel

	}

	/**
	 * Returns an array of Places in this colony. Places are ordered by tunnel, with each tunnel's places listed start to end.
	 *
	 * @return The tunnels in this colony
	 */
	public Place[] getPlaces () {
		return places.toArray(new Place[0]);
	}

	/**
	 * Returns an array of places that the bees can enter into the colony
	 *
	 * @return Places the bees can enter
	 */
	public Place[] getBeeEntrances () {
		return beeEntrances.toArray(new Place[0]);
	}

	/**
	 * Returns the queen's location
	 *
	 * @return The queen's location
	 */
	public QueenPlace getQueenPlace () {
		return queenPlace;
	}

	/**
	 * Returns the amount of available food
	 *
	 * @return the amount of available food
	 */
	public int getFood () {
		return food;
	}

	/**
	 * Increases the amount of available food
	 *
	 * @param amount
	 *            The amount to increase by
	 */
	public void increaseFood (int amount) {
		food += amount;
	}

	/**
	 * Returns if there are any bees in the queen's location (and so the game should be lost)
	 *
	 * @return if there are any bees in the queen's location
	 */
	public boolean queenHasBees () {
		return queenPlace.getBees().length > 0;
	}

	// place an ant if there is enough food available
	/**
	 * Places the given ant in the given tunnel IF there is enough available food. Otherwise has no effect
	 *
	 * @param place
	 *            Where to place the ant
	 * @param ant
	 *            The ant to place
	 */
	public void deployAnt (Place place, Ant ant) {
		if (!(place instanceof Water)) {
			if (place.getAnt() == null) {
				if (food >= ant.getFoodCost()) {
					food -= ant.getFoodCost();
					place.addInsect(ant);
				}
				else {
					System.out.println("Colony Report : \n Not enough food remains to place " + ant);
				}
			}
			else {
				if (place.getAnt() instanceof Containing) {
					Containing cont_ant = (Containing) place.getAnt();
					if (cont_ant.getInsectIn() == null && ant.containable) {
						if (food >= ant.getFoodCost()) {
							food -= ant.getFoodCost();
							place.addInsect(ant);
						}
						else {
							System.out.println("Colony Report : \n Not enough food remains to place " + ant);
						}
					}
					else {
						System.out.println("Colony Report : \n There is already an ant with a containing here of you want to add a bodyguard on an other one !");
					}
				}
				else {
					if (ant instanceof Containing) {
						if (food >= ant.getFoodCost()) {
							food -= ant.getFoodCost();
							place.addInsect(ant);
						}
						else {
							System.out.println("Colony Report : \n Not enough food remains to place " + ant);
						}
					}
					else {
						System.out.println("Colony Report : \n You can't add an ant on some other which isn't a containing here !");
					}
				}
			}
		} 
		else {
			if (ant.watersafe) {
				if (place.getAnt() == null) {
					if (food >= ant.getFoodCost()) {
						food -= ant.getFoodCost();
						place.addInsect(ant);
					}
					else {
						System.out.println("Colony Report : \n Not enough food remains to place " + ant);
					}
				}
				else {
					if (place.getAnt() instanceof Containing) {
						Containing cont_ant = (Containing) place.getAnt();
						if (cont_ant.getInsectIn() == null && ant.containable) {
							if (food >= ant.getFoodCost()) {
								food -= ant.getFoodCost();
								place.addInsect(ant);
							}
							else {
								System.out.println("Colony Report : \n Not enough food remains to place " + ant);
							}
						}
						else {
							System.out.println("Colony Report : \n There is already an ant with a containing here of you want to add a bodyguard on an other one !");
						}
					}
					else {
						if (ant instanceof Containing) {
							if (food >= ant.getFoodCost()) {
								food -= ant.getFoodCost();
								place.addInsect(ant);
							}
							else {
								System.out.println("Colony Report : \n Not enough food remains to place " + ant);
							}
						}
						else {
							System.out.println("Colony Report : \n You can't add an ant on some other which isn't a containing here !");
						}
					}
				}
			}
			else {
				System.out.println("Colony Report : \n " + ant + "can't be placed here (not watersafe)!");
			}
		}
	}

	/**
	 * Removes the ant inhabiting the given Place
	 *
	 * @param place
	 *            Where to remove the ant from
	 */
	public void removeAnt (Place place) {
		if (place.getAnt() != null && !(place.getAnt() instanceof QueenAnt)) {
			place.removeInsect(place.getAnt());
		}
	}

	/**
	 * Returns a list of all the ants currently in the colony
	 *
	 * @return a list of all the ants currently in the colony
	 */
	public ArrayList<Ant> getAllAnts () {
		ArrayList<Ant> ants = new ArrayList<Ant>();
		for (Place p : places) {
			if (p.getAnt() != null) {
				if (p.getAnt() instanceof Containing) {
					Containing cont_ant = (Containing) p.getAnt();
					if(cont_ant.getInsectIn() != null) {
						ants.add(p.getAnt());
						ants.add(cont_ant.getInsectIn());
					}
					else {
						ants.add(p.getAnt());
					}
				}
				else {
					ants.add(p.getAnt());
				}
			}
		}
		return ants;
	}

	/**
	 * Returns a list of all the bees currently in the colony
	 *
	 * @return a list of all the bees currently in the colony
	 */
	public ArrayList<Bee> getAllBees () {
		ArrayList<Bee> bees = new ArrayList<Bee>();
		for (Place p : places) {
			for (Bee b : p.getBees()) {
				bees.add(b);
			}
		}
		return bees;
	}
	
	/**
	 * Returns a list of all the bees currently dead in the colony
	 * @return a list of all the bees currently dead in the colony
	 */
	public ArrayList<Bee> getAllDeadBees () {
		ArrayList<Bee> dead_bees = new ArrayList<Bee>();
		for (Place p : places) {
			for (Bee b : p.getDeadBees()) {
				dead_bees.add(b);
			}
		}
		return dead_bees;
	}

	@Override
	public String toString () {
		return "Food: " + food + "; " + getAllBees() + "; " + getAllAnts();
	}
	
	public boolean getLvSystem() {
		return this.lv_system;
	}
}