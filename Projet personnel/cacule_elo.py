import discord
from discord.ext import commands
from discord.utils import get
import math
import json

bot = commands.Bot(command_prefix='!')


#ouvre le fichier json
with open('bp_elo.json', 'r') as fp:
    dict = json.load(fp)

###################################################################
###################################################################
#                                                                 #
#                       Parties CaculeELO                         #
#                                                                 #
###################################################################
###################################################################


@bot.event
async def on_ready():
    await bot.change_presence(status=discord.Status.idle,
                              activity=discord.Game("avec votre ELO"))
    print("Bot pret")



def Probability(rating1, rating2):

    return 1.0 * 1.0 / (1 + 1.0 * math.pow(10, 1.0 * (rating1 - rating2) / 400))

@bot.command()
async def aide(ctx):
    await ctx.send("!team [nom de l'équipe] : donne l'elo de l'équipe\n"
                   "\n"
                   "!allteam : donne tout les équipes enregistrer\n"
                   "\n"
                   "!rank [team qui a gagné] [team qui a perdu] : cacule l'elo deux équipes | commande seuleument accessible au staff")



def rang(gagnant, perdant):
    d = 1
    K = 20
    Pb = Probability(gagnant, perdant)


    Pa = Probability(perdant, gagnant)


    if (d == 1):
        gagnant = gagnant + K * (1 - Pa)
        perdant = perdant + K * (0 - Pb)


    else:
        gagnant = gagnant + K * (0 - Pa)
        perdant = perdant + K * (1 - Pb)

    #pour arrondir
    gagnant = int(gagnant)
    perdant = int(perdant)
    return  gagnant, perdant


def equipe(equipe):
    for clef in dict.keys():
        if clef == equipe:
            valeur = dict[clef]
    return valeur

def elo(equipe1, equipe2):
    gagnant = equipe(equipe1)
    perdant = equipe(equipe2)
    return rang(gagnant, perdant)


@bot.command()
@commands.has_any_role("hiiiiiii", "test")
async def rank(ctx, team1: str, team2:str):
    a = elo(team1, team2)
    dict[team1]= a[0]
    dict[team2]= a[1]
    print(dict)
    json.dump(dict,open("bp_elo.json","w"))
    await ctx.send(f"{team1} à {a[0]} d elo")
    await ctx.send(f"{team2} à {a[1]} d elo")
    await ctx.send("EN CAS DE PROBLEME CONTACTER LE STAFF OU GOLDNEY")


@rank.error
async def on_command_error(ctx,error):
    #detecter cette erreur
    if isinstance(error, commands.MissingRequiredArgument):
        await ctx.send("Erreur, vérifie le nom de l'équipe")




###################################################################
###################################################################
#                                                                 #
#                       Parties BP_ELO                            #
#                                                                 #
###################################################################
###################################################################


@bot.command()
async def team(ctx, equipe):
    for clef in dict.keys():
        if clef == equipe:
            valeur = dict[clef]
    await ctx.send(f"{equipe} = {valeur}")


@bot.command()
async def allteam(ctx):
    liste = list()
    for i in dict.keys():
        liste.append(i)
    await ctx.send(liste)
    


@bot.command()
async def add_equipe(ctx,nom, elo):
    dict[nom]=elo
    #json.dump(dict,open("bp_elo.json","w"))
    await ctx.send("l'équipe à était rajouté")
   
   
   
#idée d'autre chose a faire
#fonction qui permet à l'utilisateur d'ajouter une équipe et d'enlever




print("lancment du bot...")

bot.run("token")
