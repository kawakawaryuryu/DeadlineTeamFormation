#!/bin/sh

#### 引数 ######
#
# $1 = type1 : debug or experiment
# $2 = date1 : 日付(yyyy-MM-dd)
# $3 = time1 : 時間(HH-mm)
# $4 = type2 : debug or experiment
# $5 = date2 : 日付(yyyy-MM-dd)
# $6 = time2 : 時間(HH-mm)
#
################

type1=$1
date1=$2
time1=$3
type2=$4
date2=$5
time2=$6

path1="${HOME}/Dropbox/research/${type1}/${date1}/data"
path2="${HOME}/Dropbox/research/${type2}/${date2}/data"

get_dir() {
	local dir=$1
	local time=$2
	for file in $dir/*
	do
		if [ -d $file ]; then
			get_dir $file $time
		elif [ -f $file ]; then
			if [[ $file = *.csv ]] && [ `echo $file | grep $time` ]; then
				f=`basename $file`
				echo ${file%/${f}}
				break
			fi
		fi
	done
}

search() {
	for dir in $path1/*
	do
		local d=`basename $dir`

		# diffを取るファイルを含むディレクトリを取得
		local ret1=(`get_dir "$path1/$d" $time1`)
		local ret2=(`get_dir "$path2/$d" $time2`)


		for((i=0;i<${#ret1[*]};i++))
		do

			# ファイル名に指定の時間が含まれるもののみを取得
			files1=(`validate_file ${ret1[$i]} $time1`)
			files2=(`validate_file ${ret2[$i]} $time2`)

			# 要素数が最小の方の要素数を取得
			end=`min ${#files1[*]} ${#files2[*]}`

			for((j=0;j<$end;j++))
			do
				# 比較するファイルが適切かチェック
				success=`check ${files1[$j]} ${files2[$j]}`
				if [ $success -eq 0 ]; then
					diff_files ${files1[$j]} ${files2[$j]}
				else
					echo "ERROR!!!"
					echo ${files1[$j]}
					echo ${files2[$j]}
					break
				fi
			done

		done
	done
}

validate_file() {
	local dir=$1
	local time=$2
	for file in $dir/*
	do
		if [ `echo $file | grep $time` ]; then
			echo $file
		fi
	done
}

min() {
	if [ $1 -le $2 ]; then
		echo $1
	else
		echo $2
	fi
}

check() {
	local file1=$1
	local file2=$2

	# ファイル名からターン数、日付、時間を取り除く
	local f1=`echo $file1 | sed -e "s%$date1%%" -e "s%$time1%%" | sed -E "s%/[0-9]+t%%"`
	local f2=`echo $file2 | sed -e "s%$date2%%" -e "s%$time2%%" | sed -E "s%/[0-9]+t%%"`

	if [ $f1 = $f2 ]; then
		echo 0
	else
		echo 1
	fi
}

diff_files() {
	local path1=$1
	local path2=$2
	
	git diff --no-index $path1 $path2
}


search

